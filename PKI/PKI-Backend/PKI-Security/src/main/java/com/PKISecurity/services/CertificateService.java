package com.PKISecurity.services;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.PKISecurity.model.CertificateData;
import com.PKISecurity.model.SubjectData;
import com.PKISecurity.services.servicesImplementation.SubjectDataService;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Service;

import com.PKISecurity.Dto.CertificateDto;
import com.PKISecurity.Dto.SubjectDto;
import com.PKISecurity.certificates.CertificateGenerator;
import com.PKISecurity.data.Certificate;
import com.PKISecurity.data.Subject;
import com.PKISecurity.keystores.KeyStoreReader;
import com.PKISecurity.keystores.KeyStoreWriter;
import com.PKISecurity.services.servicesImplementation.CerificateDataService;

@Service
public class CertificateService {

	private KeyStoreReader keyStoreReader;
	private KeyStoreWriter keyStoreWriter;
	private CerificateDataService certificateDataService;
	private SubjectDataService subjectDataService;

	public CertificateService(KeyStoreReader keyStoreReader, KeyStoreWriter keyStoreWriter, CerificateDataService certificateDataService, SubjectDataService subjectDataService) {
		this.keyStoreReader = keyStoreReader;
		this.keyStoreWriter = keyStoreWriter;
		this.certificateDataService = certificateDataService;
		this.subjectDataService = subjectDataService;
	}

	public List<java.security.cert.Certificate> getAllCertificates(){
		return keyStoreReader.readAllCertificates("src/main/resources/static/keystore.jks", "password");
	}

	public HashMap<String, String> getAllIssuers(){
		List<Subject> issuers = keyStoreReader.readAllIssuersFromStore("src/main/resources/static/keystore.jks", "password".toCharArray(), "password".toCharArray());
		HashMap<String, String> issuersCNs = new HashMap<String, String>();

		for (Subject issuer : issuers) {
			String uid = issuer.getX500Name().getRDNs(BCStyle.UID)[0].getFirst().getValue().toString();
			String cn = issuer.getX500Name().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
			issuersCNs.put(uid, cn);
		}
		return issuersCNs;
	}

	public Certificate createCertificate(CertificateDto certificate) {
		Subject subject = generateSubject(certificate.subject);
		Subject issuer = null;

		if (certificate.isSelfSigned) {
			issuer = subject;
		}else if(certificate.issuerUID.length() > 0){
			issuer = getIssuerByUID(certificate.issuerUID);
		}else {
			issuer = generateSubject(certificate.issuer);
		}

		X509Certificate cert = CertificateGenerator.generateCertificate(
				subject, issuer, certificate.startDate, certificate.endDate);

		System.out.println("Kreiran novi sertifikat: ");
		System.out.println(cert);

		Certificate newCertificate = new Certificate(subject, issuer, cert.getSerialNumber().toString(), certificate.startDate, certificate.endDate, cert);
		saveCertificate(newCertificate,certificate.subject);
		return newCertificate;
	}

	private void saveCertificate(Certificate certificate,SubjectDto subjectDto) {
		keyStoreWriter.loadKeyStore("src/main/resources/static/keystore.jks",  "password".toCharArray());
		PrivateKey pk = certificate.getIssuer().getPrivateKey();
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		String alias = new String(array, Charset.forName("UTF-8"));
		//String alias = certificate.getSubject().getX500Name() + "-" + certificate.getIssuer().getX500Name();
		keyStoreWriter.write(alias , pk, "password".toCharArray(), certificate.getX509Certificate());
		keyStoreWriter.saveKeyStore("src/main/resources/static/keystore.jks",  "password".toCharArray());

		System.out.println("Cuvanje certifikata u jks fajl zavrseno.");
		saveSubjectData(subjectDto,alias,certificate.getSerialNumber());


		CertificateData newData = new CertificateData(0,alias,certificate.getSerialNumber(),false);
		certificateDataService.create(newData);

	}

	private Subject getIssuerByUID(String uid) {
		List<Subject> issuers = keyStoreReader.readAllIssuersFromStore("src/main/resources/static/keystore.jks", "password".toCharArray(), "password".toCharArray());
		Subject selectedIssuer = null;

		for (Subject issuer : issuers) {
			if (issuer.getX500Name().getRDNs(BCStyle.UID)[0].getFirst().getValue().toString().equals(uid)) {
				selectedIssuer = issuer;
				break;
			}
		}
		return selectedIssuer;
	}

	private Subject generateSubject(SubjectDto subject) {
        KeyPair kp = generateKeyPair();

        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, subject.commonName);
        builder.addRDN(BCStyle.SURNAME, subject.surname);
        builder.addRDN(BCStyle.GIVENNAME, subject.givenName);
        builder.addRDN(BCStyle.O, subject.organization);
        builder.addRDN(BCStyle.OU, subject.organizationalUnitName);
        builder.addRDN(BCStyle.C, subject.country);
        builder.addRDN(BCStyle.E, subject.email);
        builder.addRDN(BCStyle.UID, BigInteger.valueOf(System.currentTimeMillis()).toString());


		return new Subject(kp.getPrivate(), kp.getPublic(), builder.build());
    }

	public KeyPair generateKeyPair() {
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

	public void saveSubjectData(SubjectDto subjectDto, String alias, String uid){
		

		List<String> aliases = new ArrayList<String>();
		aliases.add(alias);

		SubjectData newSub = new SubjectData(uid, subjectDto.commonName, subjectDto.surname,subjectDto.givenName,subjectDto.organization,subjectDto.organizationalUnitName,subjectDto.country,subjectDto.email,aliases);
		subjectDataService.create(newSub);
	}
}