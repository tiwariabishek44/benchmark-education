package com.benchmark.education.repository;

import com.benchmark.education.entity.SalesLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SalesLedgerRegister extends JpaRepository<SalesLedger, Integer> {
    List<SalesLedger> findByEmailAndSubjectId(String email, int subjectId);

    List<SalesLedger> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM SalesLedger e WHERE e.subjectId = :subjectId AND e.email = :email")
    int deleteBySubjectIdAndEmail(@Param("subjectId") int subjectId, @Param("email") String email);

}
