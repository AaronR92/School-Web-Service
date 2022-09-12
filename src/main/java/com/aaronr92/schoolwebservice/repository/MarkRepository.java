package com.aaronr92.schoolwebservice.repository;

import com.aaronr92.schoolwebservice.entity.Mark;
import com.aaronr92.schoolwebservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    @Query("SELECT m FROM marks m ORDER BY m.id desc")
    Mark findLastMarkByUser(User user);

    Optional<Mark> findDistinctFirstByUser(User user);
}
