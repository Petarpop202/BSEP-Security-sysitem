package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String Username);
}
