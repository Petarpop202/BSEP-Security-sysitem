package com.example.bloodbank.Repository;


import com.example.bloodbank.Model.SystemAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISystemAdministratorRepository extends JpaRepository<SystemAdministrator, Long> {

}
