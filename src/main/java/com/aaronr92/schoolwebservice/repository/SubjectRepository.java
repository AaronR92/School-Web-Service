package com.aaronr92.schoolwebservice.repository;

import com.aaronr92.schoolwebservice.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByNameIgnoreCaseAndTeacher(String name, String teacher);

    Optional<Subject> findSubjectByNameIgnoreCaseAndTeacher(String name, String teacher);
}
