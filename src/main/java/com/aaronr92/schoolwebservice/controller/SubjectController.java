package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.entity.Subject;
import com.aaronr92.schoolwebservice.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping("/new")
    ResponseEntity<Map<String, String>> createNewSubject(@Valid @RequestBody Subject subject) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/subject/new")
                .toUriString());
        return ResponseEntity.created(uri).body(subjectService.addNewSubject(subject));
    }

    @DeleteMapping
    ResponseEntity<Void> deleteSubject(@RequestParam String subject,
                                    @RequestParam String teacher_username) {
        subjectService.deleteSubject(subject, teacher_username);
        return ResponseEntity.noContent().build();
    }
}
