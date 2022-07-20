package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.service.StudentService;
import com.aaronr92.schoolwebservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    UserService userService;

    @GetMapping()
    ResponseEntity<List<User>> getAllStudents() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }
}
