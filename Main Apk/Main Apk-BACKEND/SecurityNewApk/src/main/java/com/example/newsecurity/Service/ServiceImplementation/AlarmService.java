package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.Alarm;
import com.example.newsecurity.Model.MailDetails;
import com.example.newsecurity.Model.RegistrationRequest;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.IAlarmRepository;
import com.example.newsecurity.Service.IAlarmService;
import com.example.newsecurity.Service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class AlarmService implements IAlarmService {
    @Autowired
    private IAlarmRepository _repository;
    @Autowired
    private IEmailService _emailService;

    @Override
    public Iterable<Alarm> getAll() {
        return _repository.findAll();
    }

    @Override
    public Alarm getById(Long id) {
        return null;
    }

    @Override
    public Alarm create(Alarm entity) {
        return _repository.save(entity);
    }

    @Override
    public Alarm update(Alarm entity) {
        return null;
    }

    @Override
    public Alarm update() {
        List<Alarm> list = _repository.findAll();
        for(Alarm alarm : list){
            if(!alarm.isIsAlarmed())
                alarm.setIsAlarmed(true);
        }
        _repository.saveAll(list);
        return null;
    }

    @Override
    public void delete(Long entityId) {

    }

    @Override
    public boolean IsAlarmed() {
        List<Alarm> list = _repository.findAll();
        for(Alarm alarm : list){
            if(!alarm.isIsAlarmed())
                return false;
        }
        return true;
    }

    public void checkIp(String ipAddress) throws NoSuchAlgorithmException, InvalidKeyException {
        if(ipAddress.equals("192.168.1.1")) {
            Alarm alarm = new Alarm();
            alarm.setMessage("Malicious ip address : " + ipAddress + " trying to connect on application !");
            alarm.setIsAlarmed(false);
            create(alarm);
            MailSender(alarm.getMessage());
        }
    }

    public boolean MailSender(String message) throws NoSuchAlgorithmException, InvalidKeyException {
            ExecutorService executor = Executors.newFixedThreadPool(2);

            Future<?> future = executor.submit(new Runnable() {
                @Override
                public void run() {
                    MailDetails mail = new MailDetails();
                    mail.setRecipient("bsepnoreply@gmail.com");
                    mail.setSubject("Suspicios activity !");
                    mail.setMsgBody(message);
                    try {
                        _emailService.sendSimpleMail(mail);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        return true;
    }
}
