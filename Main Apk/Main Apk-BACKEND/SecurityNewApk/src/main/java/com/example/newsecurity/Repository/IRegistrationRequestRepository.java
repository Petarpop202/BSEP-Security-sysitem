package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.RegsitrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRegistrationRequestRepository extends JpaRepository<RegsitrationRequest, Long> {
}
