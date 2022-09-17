package com.aaronr92.schoolwebservice.repository;

import com.aaronr92.schoolwebservice.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findGroupByGroupNumber(int number);

    Group findGroupByGroupName(String name);

    @Query("SELECT groupName FROM s_group ")
    List<String> findAllNames();

}
