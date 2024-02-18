package com.benchmark.education.repository;

import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findByGradeAndSubjectAndStream(String grade, String subject, String stream);


}
