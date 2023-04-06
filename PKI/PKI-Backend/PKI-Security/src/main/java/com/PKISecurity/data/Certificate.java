package com.PKISecurity.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.cert.X509Certificate;
import java.util.Date;

//@Entity
//@Table(name = "Certificates")
@Getter
@Setter
@AllArgsConstructor
public class Certificate {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id", nullable = false)
    private Subject subject;
    private Subject issuer;
    private String serialNumber;
    private Date startDate;
    private Date endDate;

    // svi prethodni podaci mogu da se izvuku i iz X509Certificate, osim privatnog kljuca issuera
    private X509Certificate x509Certificate;


}
