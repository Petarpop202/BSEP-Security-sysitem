package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Repository.IEngineerRepository;
import com.example.newsecurity.Service.IEngineerService;
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
    private static final String keyStorePath = "src/main/resources/keystores/";
    @Autowired
    private IEngineerRepository engineerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Engineer newEngineer(Engineer engineer) {
        return engineerRepository.save(engineer);
    }

    @Override
    public List<Engineer> getAllEngineers() {
        return engineerRepository.findAll();
    }

    @Override
    public Engineer getEngineerById(Long id) {
        Optional<Engineer> engineer = engineerRepository.findById(id);
        if(!engineer.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
        }
        return engineer.get();
    }

    @Override
    public Engineer getEngineerByUsername(String username){
        Optional<Engineer> engineer = Optional.ofNullable(engineerRepository.findByUsername(username));
        if (!engineer.isPresent()){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
        }
        return engineer.get();
    }

    @Override
    public void deleteEngineerById(Long id) {
        engineerRepository.deleteById(id);
    }

    @Override
    public void updateEngineer(EngineerUpdateDTO engineerUpdateDTO) {
        Engineer engineer = getEngineerById(engineerUpdateDTO.getId());
        if(engineer != null){
            engineer.setName(engineerUpdateDTO.getName());
            engineer.setSurname(engineerUpdateDTO.getSurname());
            engineer.setUsername(engineerUpdateDTO.getUsername());
            engineer.setPhoneNumber(engineerUpdateDTO.getPhoneNumber());
            engineer.setJmbg(engineerUpdateDTO.getJmbg());
            engineer.setGender(engineerUpdateDTO.getGender());
            engineer.setAddress(engineerUpdateDTO.getAddress());
            engineerRepository.save(engineer);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
    }

    @Override
    public void updateEngineerSkills(EngineerUpdateSkillsDTO engineerUpdateSkillsDTO) {
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

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey secretKeyEncrypt = keyGenerator.generateKey();

            Cipher aesCipherEncrypt = Cipher.getInstance("AES");
            aesCipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKeyEncrypt);
            byte[] encryptedData = aesCipherEncrypt.doFinal(file.getBytes());

            Cipher rsaCipherEncrypt = Cipher.getInstance("RSA");
            rsaCipherEncrypt.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedAESKey = rsaCipherEncrypt.doFinal(secretKeyEncrypt.getEncoded());

            writeToKeystore(engineer.getUsername(), keyPair, encryptedAESKey);

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

    private void writeToKeystore(String username, KeyPair keyPair, byte[] encryptedAESKey) throws Exception{
        KeyStore keyStoreJKS = KeyStore.getInstance("JKS");
        char[] keystorePassword = "password".toCharArray();
        keyStoreJKS.load(null, keystorePassword);

        PrivateKey _privateKey = keyPair.getPrivate();
        char[] privateKeyPassword = "password".toCharArray();
        Certificate[] certs = new X509Certificate[]{generateSelfSignedCertificate(keyPair)};
        keyStoreJKS.setKeyEntry(username + "_privateKey", _privateKey, privateKeyPassword, certs);

        KeyStore keyStoreJCEKS = KeyStore.getInstance("JCEKS");
        keyStoreJCEKS.load(null, keystorePassword);

        byte[] _encryptedAESKey = encryptedAESKey;
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(_encryptedAESKey, "AES"));

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(keystorePassword);
        keyStoreJCEKS.setEntry(username + "_encryptedAESKey", secretKeyEntry, protectionParameter);

        try (FileOutputStream fos = new FileOutputStream(keyStorePath + username + "_keystoreJKS.jks")) {
            keyStoreJKS.store(fos, keystorePassword);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try (FileOutputStream fos = new FileOutputStream(keyStorePath + username + "_keystoreJCEKS.jks")) {
            keyStoreJCEKS.store(fos, keystorePassword);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public PrivateKey readPrivateKeyFromKeystore(String username) throws Exception{
        KeyStore keyStore = KeyStore.getInstance("JKS");
        char[] keystorePassword = "password".toCharArray();

        try (FileInputStream fis = new FileInputStream(keyStorePath + username + "_keystoreJKS.jks")) {
            keyStore.load(fis, keystorePassword);
            char[] privateKeyPassword = "password".toCharArray();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(username + "_privateKey", privateKeyPassword);

            return privateKey;
        }
    }
    public byte[] readEncryptedAESKeyFromKeystore(String username) throws Exception{
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        char[] keystorePassword = "password".toCharArray();

        try (FileInputStream fis = new FileInputStream(keyStorePath + username + "_keystoreJCEKS.jks")) {
            keyStore.load(fis, keystorePassword);

            KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(keystorePassword);
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(username + "_encryptedAESKey", protectionParameter);
            byte[] encryptedAESKey = secretKeyEntry.getSecretKey().getEncoded();
            return encryptedAESKey;
        }
    }

    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        X509V3CertificateGenerator certGenerator = new X509V3CertificateGenerator();
        certGenerator.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGenerator.setIssuerDN(new X500Principal("CN=Admin"));
        certGenerator.setSubjectDN(new X500Principal("CN=User"));
        certGenerator.setPublicKey(keyPair.getPublic());
        certGenerator.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24));
        certGenerator.setNotAfter(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365));
        certGenerator.setSignatureAlgorithm("SHA256WithRSA");

        certGenerator.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(true));

        X509Certificate cert = certGenerator.generate(keyPair.getPrivate(), "BC");

        cert.checkValidity(new Date());
        cert.verify(cert.getPublicKey());

        return cert;
    }
}
