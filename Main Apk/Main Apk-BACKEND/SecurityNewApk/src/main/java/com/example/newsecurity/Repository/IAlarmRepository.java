package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlarmRepository extends JpaRepository<Alarm,Long>{
}
