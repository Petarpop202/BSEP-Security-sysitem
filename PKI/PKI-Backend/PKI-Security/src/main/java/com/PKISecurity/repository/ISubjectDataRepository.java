package com.PKISecurity.repository;

import com.PKISecurity.model.SubjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISubjectDataRepository extends JpaRepository<SubjectData, Long> {
}
