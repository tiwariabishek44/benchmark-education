package com.benchmark.education.repository;

import com.benchmark.education.entity.ForgetPasswordTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForgetPasswordTableRepository extends JpaRepository<ForgetPasswordTable, String> {
    List<ForgetPasswordTable> findByEmail(String email);

}
