package com.example.newsecurity.Repository;


import com.example.newsecurity.Model.SystemAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISystemAdministratorRepository extends JpaRepository<SystemAdministrator, Long> {

}
