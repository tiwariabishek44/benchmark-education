package com.benchmark.education.repository;

import com.benchmark.education.entity.Manual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualRepository extends JpaRepository<Manual, Integer> {
    List<Manual> findByFileLocation(String s);

    @Query("SELECT e FROM Manual e WHERE e.id NOT IN :ids")
    List<Manual> findByIdNotIn(@Param("ids") List<Integer> ids);

    List<Manual> findBySubjectId(int subjectId);

}
