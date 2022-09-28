package com.aaronr92.schoolwebservice.repository;

import com.aaronr92.schoolwebservice.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByNameIgnoreCase(String name);

    Subject findSubjectByNameIgnoreCase(String name);
}
