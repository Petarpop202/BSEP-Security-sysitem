package com.example.newsecurity.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Service
public interface IFileService {
    byte[] encryptFile (MultipartFile file, String username) throws Exception;
    byte[] decryptFile (File file, String username) throws Exception;
    void writePrivateKeyToKeystore(String username, KeyPair keyPair) throws Exception;
    void writeEncryptedAESKeyToKeystore(String username, byte[] encryptedAESKey) throws Exception;
    PrivateKey readPrivateKeyFromKeystore(String username) throws Exception;
    byte[] readEncryptedAESKeyFromKeystore(String username) throws Exception;
    X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception;
}
