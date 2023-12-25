package com.PKISecurity.Dto;

import java.util.Date;

public class CertificateDto {
	public SubjectDto subject;
	public SubjectDto issuer;
	public Date startDate;
	public Date endDate;
	public boolean isCA;
	public boolean isSelfSigned;
	public String issuerUID;
	
	public SubjectDto getSubject() {
		return subject;
	}
	public void setSubject(SubjectDto subject) {
		this.subject = subject;
	}
	public SubjectDto getIssuer() {
		return issuer;
	}
	public void setIssuer(SubjectDto issuer) {
		this.issuer = issuer;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public boolean isCA() {
		return isCA;
	}
	public void setIsCA(boolean isCA) {
		this.isCA = isCA;
	}
	public boolean isSelfSigned() {
		return isSelfSigned;
	}
	public void setSelfSigned(boolean isSelfSigned) {
		this.isSelfSigned = isSelfSigned;
	}
	public String getIssuerUID() {
		return issuerUID;
	}
	public void setIssuerUID(String issuerUID) {
		this.issuerUID = issuerUID;
	}
}
