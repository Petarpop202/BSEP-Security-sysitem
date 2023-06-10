package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Service.IFileService;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

@Service
public class FileService implements IFileService {
    private static final String keyStorePath = "src/main/resources/keystores/";
    @Override
    public byte[] encryptFile(MultipartFile file, String username) throws Exception {
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
        writePrivateKeyToKeystore(username, keyPair);
        writeEncryptedAESKeyToKeystore(username, encryptedAESKey);

        return encryptedData;
    }

    public byte[] decryptFile (File file, String username) throws Exception {
        if (!file.exists()){
            return null;
        }
        Path path = Paths.get(file.getAbsolutePath());

        PrivateKey privateKey = readPrivateKeyFromKeystore(username);
        if (privateKey == null){
            return null;
        }
        byte[] encryptedAESKey = readEncryptedAESKeyFromKeystore(username);
        if (encryptedAESKey == null){
            return null;
        }

        Cipher rsaCipherDecrypt = Cipher.getInstance("RSA");
        rsaCipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedAESKey = rsaCipherDecrypt.doFinal(encryptedAESKey);
        SecretKey secretKeyDecrypt = new SecretKeySpec(decryptedAESKey, 0, decryptedAESKey.length, "AES");

        Cipher aesCipherDecrypt = Cipher.getInstance("AES");
        aesCipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeyDecrypt);
        byte[] decryptedData = aesCipherDecrypt.doFinal(Files.readAllBytes(path));
        return decryptedData;
    }

    @Override
    public void writePrivateKeyToKeystore(String username, KeyPair keyPair) throws Exception {
        KeyStore keyStoreJKS = KeyStore.getInstance("JKS");
        char[] keystorePassword = "password".toCharArray();
        keyStoreJKS.load(null, keystorePassword);

        PrivateKey _privateKey = keyPair.getPrivate();
        char[] privateKeyPassword = "password".toCharArray();
        Certificate[] certs = new X509Certificate[]{generateSelfSignedCertificate(keyPair)};
        keyStoreJKS.setKeyEntry(username + "_privateKey", _privateKey, privateKeyPassword, certs);

        try (FileOutputStream fos = new FileOutputStream(keyStorePath + username + "_keystoreJKS.jks")) {
            keyStoreJKS.store(fos, keystorePassword);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void writeEncryptedAESKeyToKeystore(String username, byte[] encryptedAESKey) throws Exception {
        KeyStore keyStoreJCEKS = KeyStore.getInstance("JCEKS");
        char[] keystorePassword = "password".toCharArray();
        keyStoreJCEKS.load(null, keystorePassword);

        byte[] _encryptedAESKey = encryptedAESKey;
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(_encryptedAESKey, "AES"));

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(keystorePassword);
        keyStoreJCEKS.setEntry(username + "_encryptedAESKey", secretKeyEntry, protectionParameter);

        try (FileOutputStream fos = new FileOutputStream(keyStorePath + username + "_keystoreJCEKS.jks")) {
            keyStoreJCEKS.store(fos, keystorePassword);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public PrivateKey readPrivateKeyFromKeystore(String username) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        char[] keystorePassword = "password".toCharArray();

        try (FileInputStream fis = new FileInputStream(keyStorePath + username + "_keystoreJKS.jks")) {
            keyStore.load(fis, keystorePassword);
            char[] privateKeyPassword = "password".toCharArray();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(username + "_privateKey", privateKeyPassword);

            return privateKey;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] readEncryptedAESKeyFromKeystore(String username) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        char[] keystorePassword = "password".toCharArray();

        try (FileInputStream fis = new FileInputStream(keyStorePath + username + "_keystoreJCEKS.jks")) {
            keyStore.load(fis, keystorePassword);

            KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(keystorePassword);
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(username + "_encryptedAESKey", protectionParameter);
            byte[] encryptedAESKey = secretKeyEntry.getSecretKey().getEncoded();
            return encryptedAESKey;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
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
