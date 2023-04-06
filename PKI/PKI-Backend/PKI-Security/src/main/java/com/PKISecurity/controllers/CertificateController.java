package com.PKISecurity.controllers;

import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import com.PKISecurity.keystores.KeyStoreWriter;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PKISecurity.Dto.CertificateDto;
import com.PKISecurity.Dto.SubjectDto;
import com.PKISecurity.certificates.CertificateGenerator;
import com.PKISecurity.data.Certificate;
import com.PKISecurity.data.Subject;

@Controller
@RequestMapping("certificate")
@CrossOrigin(origins = "http://localhost:3000")
public class CertificateController {

    public CertificateController() {

    }

    @PostMapping("/create")
    Certificate CreateCertificate(@RequestBody CertificateDto certificate) {

        Subject subject = generateSubject(certificate.subject, "123456");
        Subject issuer = generateSubject(certificate.issuer, "654321");
        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();

        X509Certificate cert = CertificateGenerator.generateCertificate(
                subject, issuer, certificate.startDate, certificate.endDate, "1");
        System.out.println("Kreiran novi sertifikat: ");
        System.out.println(cert);




        // Upis sertifikata u jks fajl

        // Inicijalizacija fajla za cuvanje sertifikata
        System.out.println("Cuvanje certifikata u jks fajl:");
        keyStoreWriter.loadKeyStore("src/main/resources/static/root-certificate.jks",  "password".toCharArray());
        PrivateKey pk = issuer.getPrivateKey();
        keyStoreWriter.write(certificate.subject.organization, pk, "password".toCharArray(), cert);
        keyStoreWriter.saveKeyStore("src/main/resources/static/root-certificate.jks",  "password".toCharArray());
        System.out.println("Cuvanje certifikata u jks fajl zavrseno.");






        try {
            cert.verify(issuer.getPublicKey());
            System.out.println("Verifikacija uspesna");
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }

        return new Certificate(subject, issuer, "1", certificate.startDate, certificate.endDate, cert);
    }

    private Subject generateSubject(SubjectDto subject, String uid) {
        KeyPair kp = generateKeyPair();

        //klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, subject.commonName);
        builder.addRDN(BCStyle.SURNAME, subject.surname);
        builder.addRDN(BCStyle.GIVENNAME, subject.givenName);
        builder.addRDN(BCStyle.O, subject.organization);
        builder.addRDN(BCStyle.OU, subject.organizationalUnitName);
        builder.addRDN(BCStyle.C, subject.country);
        builder.addRDN(BCStyle.E, subject.email);
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, uid);

        return new Subject(kp.getPrivate(), kp.getPublic(), builder.build());
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
