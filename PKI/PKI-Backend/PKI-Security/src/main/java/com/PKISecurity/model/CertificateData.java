package com.PKISecurity.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Certificate")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column
    private String alias;
    @Column
    private String serialNumber;
    @Column
    private boolean revoke;
}
