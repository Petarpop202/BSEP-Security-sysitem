package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITestRepository extends JpaRepository<Test,Long> {
}
