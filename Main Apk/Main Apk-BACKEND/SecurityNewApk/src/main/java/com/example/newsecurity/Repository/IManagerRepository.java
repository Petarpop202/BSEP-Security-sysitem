package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IManagerRepository extends JpaRepository<Manager, Long> {
}
