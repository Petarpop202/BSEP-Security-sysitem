package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.RequestResponse;
import com.example.newsecurity.Model.MailDetails;
import com.example.newsecurity.Model.RegistrationRequest;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.IRegistrationRequestRepository;
import com.example.newsecurity.Repository.IUserRepository;
import com.example.newsecurity.Service.IEmailService;
import com.example.newsecurity.Service.IRegistrationRequestService;
import com.example.newsecurity.Service.IUserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class RegistrationRequestService implements IRegistrationRequestService {
    private IRegistrationRequestRepository _registrationRequestRepository;
    private static IUserRepository _userRepository;
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    @Autowired
    private IEmailService _emailService;
    RegistrationRequestService(IRegistrationRequestRepository registrationRequestRepository, IUserRepository userRepository){_registrationRequestRepository = registrationRequestRepository;_userRepository = userRepository;}
    @Override
    public Iterable<RegistrationRequest> getAll() {
        return _registrationRequestRepository.findAll();
    }

    @Override
    public RegistrationRequest getById(Long id) {
        return null;
    }

    @Override
    public RegistrationRequest create(RegistrationRequest entity) {
        return _registrationRequestRepository.save(entity);
    }

    @Override
    public RegistrationRequest update(RegistrationRequest entity) {
        return null;
    }

    @Override
    public void delete(Long entityId) {

    }

    @Override
    public RegistrationRequest setResponse(RequestResponse response) throws NoSuchAlgorithmException, InvalidKeyException {
        RegistrationRequest registrationRequest = _registrationRequestRepository.findById(response.getRequestId()).orElseGet(null);
        registrationRequest.setAccepted(response.isResponse());
        registrationRequest.setResponseDate(LocalDate.now());
        MailSender(registrationRequest);
        return _registrationRequestRepository.save(registrationRequest);
    }

    @Override
    public void Activate(String code) throws NoSuchAlgorithmException, InvalidKeyException {
        String key = "secretKey";
        User user = verifyHmac(code,key);
        if(user != null){
            user.setEnabled(true);
            enable(user);
        }
    }

    @Override
    public List<RegistrationRequest> getAllUnresponded() {
        List<RegistrationRequest> list = _registrationRequestRepository.findAll();
        List<RegistrationRequest> returnList = new ArrayList<RegistrationRequest>();
        for (RegistrationRequest request : list)
        {
            if(request.getResponseDate() == null)
                returnList.add(request);
        }
        return returnList;
    }

    public boolean MailSender(RegistrationRequest registrationRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        User user = _userRepository.findByUsername(registrationRequest.getUserUsername());
        if(registrationRequest.isAccepted()) {
            userResponseApprove(user);
            ExecutorService executor = Executors.newFixedThreadPool(2);
            String key = "secretKey";
            String verification = HmacCoder(registrationRequest.getUserUsername(),key);

            Future<?> future = executor.submit(new Runnable() {
                @Override
                public void run() {
                    MailDetails mail = new MailDetails();
                    mail.setRecipient(user.getMail());
                    mail.setSubject("Verifikacija naloga !");
                    mail.setMsgBody("Kako biste verifikovali vas nalog potrebno je da odete na sledeci link :" +
                            " http://localhost:8080/auth/activate?code=" + verification);
                    try {
                        _emailService.sendSimpleMail(mail);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        else {
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<?> future = executor.submit(new Runnable() {
                @Override
                public void run() {
                    MailDetails mail = new MailDetails();
                    mail.setRecipient(user.getMail());
                    mail.setSubject("Verifikacija naloga !");
                    mail.setMsgBody("Na zalost verifikacija vaseg naloga nije uspela :(");
                    try {
                        _emailService.sendSimpleMail(mail);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return true;
    }

    private void userResponseApprove(User user) {
        user.setRequestApproved(true);
        User old = _userRepository.findByUsername(user.getUsername());
        old.setRequestApproved(user.isRequestApproved());
        _userRepository.save(old);
    }

    public static String HmacCoder(String username, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(keySpec);
        byte[] hmacBytes = mac.doFinal(username.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    public static User verifyHmac(String hmac, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        for(User user : _userRepository.findAll()){
            String generatedHmac = HmacCoder(user.getUsername(), secretKey);
            if(generatedHmac.equals(hmac))
                return user;
        }

        return null;
    }

    public User enable(User u) {
        User old = _userRepository.findByUsername(u.getUsername());
        old.setEnabled(u.isEnabled());
        return _userRepository.save(old);
    }
}
