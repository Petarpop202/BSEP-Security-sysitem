package com.PKISecurity.controllers;

import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;

import com.PKISecurity.model.CertificateData;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PKISecurity.Dto.CertificateDto;
import com.PKISecurity.data.Certificate;
import com.PKISecurity.services.CertificateService;

@RestController
@RequestMapping("certificate")
@CrossOrigin(origins = "http://localhost:3000")
public class CertificateController {
	
	private CertificateService certificateService;
	
	public CertificateController(CertificateService certificateService) {
		this.certificateService = certificateService;
	}
	
	@GetMapping()
	ResponseEntity<List<String>> GetCertificates(){
		List<java.security.cert.Certificate> certificates = certificateService.getAllCertificates();
		
		List<String> encodedCertificates = new ArrayList<String>();
		Encoder encoder = Base64.getEncoder();
		
		for (java.security.cert.Certificate cert : certificates) {
			try {
				StringBuilder builder = new StringBuilder();
				builder.append("-----BEGIN CERTIFICATE-----\n");
				builder.append(encoder.encodeToString(cert.getEncoded()));
				builder.append("\n-----END CERTIFICATE-----");
				encodedCertificates.add(builder.toString());
			} catch (CertificateEncodingException e) {
				e.printStackTrace();
			}
		}
		HttpHeaders headers = new HttpHeaders();
	    return new ResponseEntity<>(encodedCertificates, headers, HttpStatus.OK);
	}
	
	@GetMapping("/issuers")
	HashMap<String, String> GetIssuers(){
		return certificateService.getAllIssuers();
	}
	
	@PostMapping("/create")
	ResponseEntity<String> CreateCertificate(@RequestBody CertificateDto certificate) {
		
		Certificate cert = certificateService.createCertificate(certificate);
		Encoder encoder = Base64.getEncoder();
		StringBuilder builder = new StringBuilder();
		
		try {
			builder.append("-----BEGIN CERTIFICATE-----\n");
			builder.append(encoder.encodeToString(cert.getX509Certificate().getEncoded()));
			builder.append("\n-----END CERTIFICATE-----");
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<>(builder.toString(), headers, HttpStatus.OK);
	}

	@GetMapping("/revoke")
	ResponseEntity<List<String>> RevokeCertificate() throws IOException, OperatorCreationException, CRLException, CertificateException {
		certificateService.revokeCertificate("Nista");
		return null;
	}
}
