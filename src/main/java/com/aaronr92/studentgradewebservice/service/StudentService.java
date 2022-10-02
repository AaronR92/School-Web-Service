package com.aaronr92.studentgradewebservice.service;

import com.aaronr92.studentgradewebservice.entity.User;
import com.aaronr92.studentgradewebservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    UserRepository userRepository;

    //TODO
    // Add get by students role
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok((List<User>) userRepository.findAll());
    }
}
