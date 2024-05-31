package com.benchmark.education.repository;

import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.Manual;
import com.benchmark.education.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findByGradeAndSubjectAndStream(String grade, String subject, String stream);


    List<Subject> findByIdIn(List<Integer> subjectList);

    @Query("SELECT e FROM Subject e WHERE e.id NOT IN :ids")
    List<Subject> findByIdNotIn(@Param("ids") List<Integer> ids);
}
