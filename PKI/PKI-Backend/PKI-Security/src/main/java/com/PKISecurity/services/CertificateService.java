package com.PKISecurity.services;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import com.PKISecurity.model.CertificateData;
import com.PKISecurity.model.SubjectData;
import com.PKISecurity.services.servicesImplementation.SubjectDataService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.TBSCertList;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;

import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import com.PKISecurity.Dto.CertificateDto;
import com.PKISecurity.Dto.SubjectDto;
import com.PKISecurity.certificates.CertificateGenerator;
import com.PKISecurity.data.Certificate;
import com.PKISecurity.data.Subject;
import com.PKISecurity.keystores.KeyStoreReader;
import com.PKISecurity.keystores.KeyStoreWriter;
import com.PKISecurity.services.servicesImplementation.CerificateDataService;

import javax.security.auth.x500.X500Principal;

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

	public HashMap<String, String> getAllIssuers() throws CertificateException, IOException, OperatorCreationException, CRLException {
		List<Subject> issuers = keyStoreReader.readAllIssuersFromStore("src/main/resources/static/keystore.jks", "password".toCharArray(), "password".toCharArray());
		HashMap<String, String> issuersCNs = new HashMap<String, String>();

		for (Subject issuer : issuers) {
			for(java.security.cert.Certificate certificate: keyStoreReader.readAllCertificates("src/main/resources/static/keystore.jks", "password")){
				String uid = issuer.getX500Name().getRDNs(BCStyle.UID)[0].getFirst().getValue().toString();
				X509Certificate cert = (X509Certificate) certificate;
				X500Name name = new X500Name(cert.getSubjectX500Principal().toString());


				if (uid.equals(name.getRDNs(BCStyle.UID)[0].getFirst().getValue().toString())){
					if(verifyCertificate("0" + cert.getSerialNumber().toString(16))){

						String cn = issuer.getX500Name().getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
						issuersCNs.put(uid, cn);
					}
				}
			}

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
				subject, issuer, certificate.startDate, certificate.endDate, certificate.isCA);

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

		SubjectData newSub = new SubjectData(0L, subjectDto.commonName, subjectDto.surname,subjectDto.givenName,subjectDto.organization,subjectDto.organizationalUnitName,subjectDto.country,subjectDto.email,aliases);
		subjectDataService.create(newSub);
	}

	public void revokeCertificatesByIssuer(String serialNum) throws CertificateException, IOException, OperatorCreationException, CRLException {
		revokeCertificate(serialNum);
		String alias = keyStoreReader.getAlias(serialNum,"src/main/resources/static/keystore.jks", "password".toCharArray());
		java.security.cert.Certificate cert  = keyStoreReader.readCertificate("src/main/resources/static/keystore.jks", "password", alias);

		X509Certificate mainCert = (X509Certificate)cert;
		X500Principal subj = mainCert.getSubjectX500Principal();

		List<java.security.cert.Certificate> certificates = keyStoreReader.readAllCertificates("src/main/resources/static/keystore.jks", "password");
		for(java.security.cert.Certificate certificate : certificates) {
			X509Certificate newCert = (X509Certificate) certificate;
			if(subj.equals(newCert.getIssuerX500Principal()) && !newCert.getIssuerX500Principal().equals(newCert.getSubjectX500Principal()))
				revokeCertificatesByIssuer("0" + newCert.getSerialNumber().toString(16));
		}
	}


	public X509Certificate revokeCertificate(String serialNum) throws CRLException, IOException, OperatorCreationException, CertificateException {
		String alias = keyStoreReader.getAlias(serialNum,"src/main/resources/static/keystore.jks", "password".toCharArray());
		PrivateKey pk = keyStoreReader.readPrivateKey("src/main/resources/static/keystore.jks","password","�[�u��\u0016","password");
		Subject issuer = keyStoreReader.readIssuerFromStore("src/main/resources/static/keystore.jks","�[�u��\u0016", "password".toCharArray(), "password".toCharArray());

		java.security.cert.Certificate revokedCertificate = keyStoreReader.readCertificate("src/main/resources/static/keystore.jks",  "password",alias);
		X509Certificate revoked = (X509Certificate)revokedCertificate;


		InputStream crlInputStream = new FileInputStream("src/main/resources/static/crl.crl");
		X509CRLHolder crlHolder1 = new X509CRLHolder(crlInputStream);


		X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(issuer.getX500Name(), Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		crlBuilder.addCRLEntry(revoked.getSerialNumber(), Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), CRLReason.privilegeWithdrawn);
		crlBuilder.addCRL(crlHolder1);

		ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(pk);
		X509CRLHolder crlHolder = crlBuilder.build(contentSigner);
		X509CRL crl = new JcaX509CRLConverter().getCRL(crlHolder);

		FileOutputStream fos = new FileOutputStream("src/main/resources/static/crl.crl");
		fos.write(crl.getEncoded());
		fos.close();


		return revoked;
	}

	public boolean verifyCertificate(String serialNum) throws CRLException, IOException, OperatorCreationException, CertificateException {
		String alias = keyStoreReader.getAlias(serialNum,"src/main/resources/static/keystore.jks", "password".toCharArray());
		Subject issuer = keyStoreReader.readIssuerFromStore("src/main/resources/static/keystore.jks",alias, "password".toCharArray(), "password".toCharArray());

		java.security.cert.Certificate cert = keyStoreReader.readCertificate("src/main/resources/static/keystore.jks",  "password",alias);
		X509Certificate toVerify = (X509Certificate)cert;

		if(!verifyRevoke(toVerify) || !verifyDate(toVerify)) return false;
		for(java.security.cert.Certificate certificate: keyStoreReader.readAllCertificates("src/main/resources/static/keystore.jks",  "password")){
			X509Certificate check = (X509Certificate) certificate;
			if(check.getSubjectX500Principal().equals(toVerify.getIssuerX500Principal()) && !check.getSubjectX500Principal().equals(check.getIssuerX500Principal())){
				verifyCertificate("0" + check.getSerialNumber().toString(16));
			}
		}





		return true;
	}

	public boolean verifyRevoke(X509Certificate certificate) throws IOException, CertificateException, CRLException {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			FileInputStream fis = new FileInputStream("src/main/resources/static/crl.crl");
			X509CRL crl = (X509CRL) cf.generateCRL(fis);

			Set revokedCertificates = crl.getRevokedCertificates();
			Iterator it = revokedCertificates.iterator();
			while (it.hasNext()) {
				X509CRLEntry entry = (X509CRLEntry) it.next();
//				System.out.println("Serial Number: " + entry.getSerialNumber());
				if(entry.getSerialNumber().equals(certificate.getSerialNumber())){
					return false;
				}
			}

		} catch (CRLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}



		return true;
	}

	public boolean verifyDate(X509Certificate certificate) {
		Date expirationDate = certificate.getNotAfter();
		Date currentDate = new Date();
		if(currentDate.after(expirationDate)) return false;
		return true;
	}

//	public boolean verifyCertificateSignature(X509Certificate certificate, Subject issuer){
//		try {
//			certificate.verify(issuer.getPublicKey());
//		}
//	}
}
