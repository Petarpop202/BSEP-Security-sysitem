package com.example.newsecurity.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
@Service
public interface IEncryptService {
    String encryptFile (String stringToEncrypt, String username, String encryptionType) throws Exception;
    String decryptFile (String stringToEncrypt, String username, String encryptionType) throws Exception;
    void writePrivateKeyToKeystore(String username, KeyPair keyPair, String encryptionType) throws Exception;
    void writeEncryptedAESKeyToKeystore(String username, byte[] encryptedAESKey, String encryptionType) throws Exception;
    PrivateKey readPrivateKeyFromKeystore(String username, String encryptionType) throws Exception;
    byte[] readEncryptedAESKeyFromKeystore(String username, String encryptionType) throws Exception;
    X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception;
}
