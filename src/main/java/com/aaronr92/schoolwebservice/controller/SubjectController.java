package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.entity.Subject;
import com.aaronr92.schoolwebservice.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping("/new")
    ResponseEntity<Map<String, String>> createNewSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok().body(subjectService.addNewSubject(subject));
    }

    @DeleteMapping
    ResponseEntity<?> deleteSubject(@RequestParam String name) {
        subjectService.deleteSubject(name);
        return (ResponseEntity<?>) ResponseEntity.noContent();
    }
}
