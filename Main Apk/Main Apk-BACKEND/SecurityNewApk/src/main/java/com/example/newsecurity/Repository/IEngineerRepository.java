package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.Engineer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEngineerRepository extends JpaRepository<Engineer, Long> {
}
