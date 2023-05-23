package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.UserRequest;
import com.example.newsecurity.Model.MailDetails;
import com.example.newsecurity.Model.RegistrationRequest;
import com.example.newsecurity.Model.Role;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.IUserRepository;
import com.example.newsecurity.Service.IRegistrationRequestService;
import com.example.newsecurity.Service.IRoleService;
import com.example.newsecurity.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class UserService implements IUserService {
    private IUserRepository _userRepository;
    private IRoleService _roleService;
    private IRegistrationRequestService _registrationRequestService;

    UserService(IUserRepository userRepository,IRoleService roleService,IRegistrationRequestService registrationRequestService){_userRepository = userRepository;_roleService = roleService;_registrationRequestService=registrationRequestService;}
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public Iterable<User> getAll() {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void delete(Long entityId) {}

    @Override
    public User findByUsername(String username) {
        return _userRepository.findByUsername(username);
    }

    @Override
    public User save(UserRequest userRequest) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUserUsername(userRequest.getUsername());
        _registrationRequestService.create(registrationRequest);
        User u = new User();
        u.setUsername(userRequest.getUsername());
        // pre nego sto postavimo lozinku u atribut hesiramo je kako bi se u bazi nalazila hesirana lozinka
        // treba voditi racuna da se koristi isi password encoder bean koji je postavljen u AUthenticationManager-u kako bi koristili isti algoritam
        u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        u.setName(userRequest.getName());
        u.setSurname(userRequest.getSurname());
        u.setEnabled(false);
        u.setRequestApproved(false);
        u.setAddress(userRequest.getAddress());
        u.setJmbg(userRequest.getJmbg());
        u.setGender(userRequest.getGender());
        u.setMail(userRequest.getMail());
        u.setPhoneNumber(userRequest.getPhoneNumber());
        u.setTitle(userRequest.getTitle());
        // u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
        Role roles1 = _roleService.findByName(userRequest.getRole());
        List<Role> r = new ArrayList<>();
        r.add(roles1);
        u.setRoles(r);

        return this._userRepository.save(u);
    }

    @Override
    public User activate(User u) {
        User old = _userRepository.findByUsername(u.getUsername());
        old.setEnabled(u.isEnabled());
        return _userRepository.save(old);
    }

    @Override
    public void passwordlessLogin(String token, String email) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future = executor.submit(new Runnable() {
            @Override
            public void run() {
                MailDetails mail = new MailDetails();
                mail.setSubject("Logovanje mejlom!");
                mail.setRecipient(email);
                mail.setMsgBody("Da bi ste se ulogovali kliknite na sledeci link imate 10 minuta :" +
                        " http://localhost:8080/auth/passwordlessLoginActivate?code=" + token);
                try {
                    emailService.sendSimpleMail(mail);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
