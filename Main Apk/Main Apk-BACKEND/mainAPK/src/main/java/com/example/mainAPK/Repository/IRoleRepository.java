package com.example.mainAPK.Repository;


import com.example.mainAPK.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
