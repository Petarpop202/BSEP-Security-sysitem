package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Service.IEncryptService;
import com.example.newsecurity.Service.IFileService;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

@Service
public class EncryptService implements IEncryptService {
    private static final String keyStorePath = "src/main/resources/encrypt/";
    @Value("${keystore.password}")
    private String password;

    public String encryptFile(String stringToEncrypt, String username, String encryptionType) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKeyEncrypt = keyGenerator.generateKey();

        Cipher aesCipherEncrypt = Cipher.getInstance("AES");
        aesCipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKeyEncrypt);
        if(encryptionType.equals("mail")){
            byte[] encryptedData = aesCipherEncrypt.doFinal(stringToEncrypt.getBytes());
            Cipher rsaCipherEncrypt = Cipher.getInstance("RSA");
            rsaCipherEncrypt.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedAESKey = rsaCipherEncrypt.doFinal(secretKeyEncrypt.getEncoded());
            writePrivateKeyToKeystore(username, keyPair, encryptionType);
            writeEncryptedAESKeyToKeystore(username, encryptedAESKey, encryptionType);

            return Base64.getEncoder().encodeToString(encryptedData);
        } else {
            byte[] encryptedData = aesCipherEncrypt.doFinal(stringToEncrypt.getBytes(StandardCharsets.UTF_8));
            Cipher rsaCipherEncrypt = Cipher.getInstance("RSA");
            rsaCipherEncrypt.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedAESKey = rsaCipherEncrypt.doFinal(secretKeyEncrypt.getEncoded());
            writePrivateKeyToKeystore(username, keyPair, encryptionType);
            writeEncryptedAESKeyToKeystore(username, encryptedAESKey, encryptionType);

            return Base64.getEncoder().encodeToString(encryptedData);
        }



    }

    public String decryptFile (String encryptedData, String username, String encryptionType) throws Exception {
        if (encryptedData == null){
            return null;
        }

        PrivateKey privateKey = readPrivateKeyFromKeystore(username, encryptionType);
        if (privateKey == null){
            return null;
        }
        byte[] encryptedAESKey = readEncryptedAESKeyFromKeystore(username, encryptionType);
        if (encryptedAESKey == null){
            return null;
        }

        Cipher rsaCipherDecrypt = Cipher.getInstance("RSA");
        rsaCipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedAESKey = rsaCipherDecrypt.doFinal(encryptedAESKey);
        SecretKey secretKeyDecrypt = new SecretKeySpec(decryptedAESKey, 0, decryptedAESKey.length, "AES");

        Cipher aesCipherDecrypt = Cipher.getInstance("AES");
        aesCipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeyDecrypt);

        byte[] decryptedBytes = aesCipherDecrypt.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public void writePrivateKeyToKeystore(String username, KeyPair keyPair, String encryptionType) throws Exception {
        KeyStore keyStoreJKS = KeyStore.getInstance("JKS");
        char[] keystorePassword = password.toCharArray();
        keyStoreJKS.load(null, keystorePassword);

        PrivateKey _privateKey = keyPair.getPrivate();
        char[] privateKeyPassword = password.toCharArray();
        Certificate[] certs = new X509Certificate[]{generateSelfSignedCertificate(keyPair)};
        keyStoreJKS.setKeyEntry(username + encryptionType + "_privateKey", _privateKey, privateKeyPassword, certs);

        try (FileOutputStream fos = new FileOutputStream(keyStorePath + username + encryptionType + "_keystoreJKS.jks")) {
            keyStoreJKS.store(fos, keystorePassword);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeEncryptedAESKeyToKeystore(String username, byte[] encryptedAESKey,  String encryptionType) throws Exception {
        KeyStore keyStoreJCEKS = KeyStore.getInstance("JCEKS");
        char[] keystorePassword = password.toCharArray();
        keyStoreJCEKS.load(null, keystorePassword);

        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(encryptedAESKey, "AES"));

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(keystorePassword);
        keyStoreJCEKS.setEntry(username + encryptionType + "_encryptedAESKey", secretKeyEntry, protectionParameter);

        try (FileOutputStream fos = new FileOutputStream(keyStorePath + username + encryptionType + "_keystoreJCEKS.jks")) {
            keyStoreJCEKS.store(fos, keystorePassword);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public PrivateKey readPrivateKeyFromKeystore(String username,  String encryptionType) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        char[] keystorePassword = password.toCharArray();

        try (FileInputStream fis = new FileInputStream(keyStorePath + username + encryptionType + "_keystoreJKS.jks")) {
            keyStore.load(fis, keystorePassword);
            char[] privateKeyPassword = password.toCharArray();
            return (PrivateKey) keyStore.getKey(username + encryptionType + "_privateKey", privateKeyPassword);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public byte[] readEncryptedAESKeyFromKeystore(String username,  String encryptionType) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        char[] keystorePassword = password.toCharArray();

        try (FileInputStream fis = new FileInputStream(keyStorePath + username + encryptionType + "_keystoreJCEKS.jks")) {
            keyStore.load(fis, keystorePassword);

            KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(keystorePassword);
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(username + encryptionType + "_encryptedAESKey", protectionParameter);
            return secretKeyEntry.getSecretKey().getEncoded();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
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
