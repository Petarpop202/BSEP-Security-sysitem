package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRegistrationRequestRepository extends JpaRepository<RegistrationRequest, Long> {
}
