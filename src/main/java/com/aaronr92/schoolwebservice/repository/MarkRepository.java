package com.aaronr92.schoolwebservice.repository;

import com.aaronr92.schoolwebservice.entity.Mark;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkRepository extends CrudRepository<Mark, Long> {
}
