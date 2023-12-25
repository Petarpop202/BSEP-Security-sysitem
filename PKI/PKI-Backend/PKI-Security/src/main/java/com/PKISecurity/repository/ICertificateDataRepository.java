package com.PKISecurity.repository;

import com.PKISecurity.model.CertificateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICertificateDataRepository extends JpaRepository<CertificateData, Long> {
}
