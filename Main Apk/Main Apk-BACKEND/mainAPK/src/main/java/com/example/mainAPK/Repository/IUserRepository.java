package com.example.mainAPK.Repository;

import com.example.mainAPK.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String Username);
}
