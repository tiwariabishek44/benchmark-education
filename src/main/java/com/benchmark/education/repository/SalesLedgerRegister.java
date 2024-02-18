package com.benchmark.education.repository;

import com.benchmark.education.entity.SalesLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesLedgerRegister extends JpaRepository<SalesLedger, Integer> {
    List<SalesLedger> findByStudentIdAndSubjectId(int studentId, int subjectId);
}
