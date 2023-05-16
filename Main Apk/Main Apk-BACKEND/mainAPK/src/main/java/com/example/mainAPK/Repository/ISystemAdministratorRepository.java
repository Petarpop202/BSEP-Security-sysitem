package com.example.mainAPK.Repository;


import com.example.mainAPK.Model.SystemAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISystemAdministratorRepository extends JpaRepository<SystemAdministrator, Long> {

}
