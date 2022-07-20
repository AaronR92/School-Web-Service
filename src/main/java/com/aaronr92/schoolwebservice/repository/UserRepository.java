package com.aaronr92.schoolwebservice.repository;

import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.util.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsUserByEmailIgnoreCase(String email);

    boolean existsUserByPhone(String phone);

    boolean existsUserByUsername(String username);

    Optional<User> findUserByUsername(String email);
}
