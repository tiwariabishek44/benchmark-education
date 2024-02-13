package com.benchmark.education.repository;

import com.benchmark.education.entity.LoginSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginSessionRepository extends JpaRepository<LoginSession, Integer> {
}
