package com.PKISecurity.controllers;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

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
		
		X509Certificate cert = CertificateGenerator.generateCertificate(
				subject, issuer, certificate.startDate, certificate.endDate, "1");
		System.out.println("Kreiran novi sertifikat: ");
		System.out.println(cert);
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
