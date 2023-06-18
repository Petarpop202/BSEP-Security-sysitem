package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Repository.IEngineerRepository;
import com.example.newsecurity.Service.IEncryptService;
import com.example.newsecurity.Service.IEngineerService;
import com.example.newsecurity.Service.IFileService;
import com.sun.jarsigner.ContentSigner;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class EngineerService implements IEngineerService {
    private static final String path = "src/main/resources/cv";

    @Autowired
    private IEngineerRepository engineerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IFileService fileService;
    @Autowired
    private IEncryptService encryptService;


    @Override
    public Engineer newEngineer(Engineer engineer) {
        return engineerRepository.save(engineer);
    }

    @Override
    public List<Engineer> getAllEngineers() throws Exception {
        List<Engineer> engineers = engineerRepository.findAll();
        List<Engineer> engineersDecrypted = new ArrayList<>();
        if (engineers == null){
            return null;
        }
        for (Engineer engineer : engineers){
            engineersDecrypted.add(readEngineer(engineer));
        }
        return engineersDecrypted;
    }

    @Override
    public Engineer getEngineerById(Long id) throws Exception {
        Optional<Engineer> engineerOptional = engineerRepository.findById(id);
        if(!engineerOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
        }
        return readEngineer(engineerOptional.get());
    }

    @Override
    public Engineer getEngineerByUsername(String username) throws Exception {
        Optional<Engineer> engineerOptional = Optional.ofNullable(engineerRepository.findByUsername(username));
        if (!engineerOptional.isPresent()){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
        }
//        Engineer engineer = engineerOptional.get();
//        engineer.setPhoneNumber(encryptService.decryptFile(engineerOptional.get().getPhoneNumber(), engineerOptional.get().getUsername(), "phoneNumber"));
//        engineer.setJmbg(encryptService.decryptFile(engineerOptional.get().getJmbg(), engineerOptional.get().getUsername(), "jmbg"));
//        engineer.setMail(encryptService.decryptFile(engineerOptional.get().getMail(), engineerOptional.get().getUsername(), "mail"));
        return readEngineer(engineerOptional.get());
    }

    @Override
    public void deleteEngineerById(Long id) {
        engineerRepository.deleteById(id);
    }

    @Override
    public void updateEngineer(EngineerUpdateDTO engineerUpdateDTO) throws Exception {
        Engineer engineer = getEngineerById(engineerUpdateDTO.getId());
        String engineerMail = engineer.getMail();
        if(engineer != null){
            engineer.setName(engineerUpdateDTO.getName());
            engineer.setSurname(engineerUpdateDTO.getSurname());
            engineer.setUsername(engineerUpdateDTO.getUsername());
            engineer.setPhoneNumber(encryptService.encryptFile(engineerUpdateDTO.getPhoneNumber(), engineer.getUsername(), "phoneNumber"));
            engineer.setJmbg(encryptService.encryptFile(engineerUpdateDTO.getJmbg(), engineer.getUsername(), "jmbg"));
            engineer.setMail(encryptService.encryptFile(engineerMail, engineer.getUsername(), "mail"));
            engineer.setGender(engineerUpdateDTO.getGender());
            engineer.setAddress(engineerUpdateDTO.getAddress());
            engineerRepository.save(engineer);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
    }

    @Override
    public void updateEngineerSkills(EngineerUpdateSkillsDTO engineerUpdateSkillsDTO) throws Exception {
        Engineer engineer = getEngineerById(engineerUpdateSkillsDTO.getId());
        if(engineer != null){
            engineer.setSkills(engineerUpdateSkillsDTO.getSkills());
            engineerRepository.save(engineer);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
    }
    @Override
    public Engineer updatePassword(Long id, String newPassword) {
        Optional<Engineer> optionalEngineer = engineerRepository.findById(id);
        if(!optionalEngineer.isPresent()){
            throw new NoSuchElementException("Engineer not found!");
        }
        Engineer engineer = optionalEngineer.get();
        engineer.setPassword(passwordEncoder.encode(newPassword));

        return engineerRepository.save(engineer);
    }
    public String uploadCV(Long id, MultipartFile file) throws Exception {
        Engineer engineer = getEngineerById(id);
        if (!file.getOriginalFilename().isEmpty()){
            byte[] encryptedData = fileService.encryptFile(file, engineer.getUsername());
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path, engineer.getUsername() + "_CV.pdf")));
            outputStream.write(encryptedData);
            outputStream.flush();
            outputStream.close();
        } else {
            throw new Exception();
        }
        File files = new File(path);
        return Arrays.stream(files.list()).filter(cv -> cv.contains(engineer.getUsername())).toList().get(0);
    }

    public Engineer readEngineer(Engineer engineerToRead) throws Exception {
        Engineer engineer = engineerToRead;
        if(engineer.getSurname().equals("Inzenjer")){
            return engineer;
        }
        engineer.setPhoneNumber(encryptService.decryptFile(engineerToRead.getPhoneNumber(), engineerToRead.getUsername(), "phoneNumber"));
        engineer.setJmbg(encryptService.decryptFile(engineerToRead.getJmbg(), engineerToRead.getUsername(), "jmbg"));
        engineer.setMail(encryptService.decryptFile(engineerToRead.getMail(), engineerToRead.getUsername(), "mail"));
        return engineer;
    }
}
