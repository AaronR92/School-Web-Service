package com.aaronr92.schoolwebservice.repository;

import com.aaronr92.schoolwebservice.entity.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    boolean existsByNameIgnoreCase(String name);

    Subject findSubjectByNameIgnoreCase(String name);
}
