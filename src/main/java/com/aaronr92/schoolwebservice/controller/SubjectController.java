package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.dto.SubjectDTO;
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

    @PostMapping
    public ResponseEntity<Map<String, String>> createNewSubject(@Valid @RequestBody SubjectDTO subject) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/subject")
                .toUriString());
        return ResponseEntity.created(uri).body(subjectService.addNewSubject(subject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateTeacher(@PathVariable Long id, @RequestParam String operation, @RequestParam String teacher) {
        return ResponseEntity.ok(subjectService.updateTeacher(id, operation, teacher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateName(@PathVariable Long id, @RequestParam String name) {
        return ResponseEntity.ok(subjectService.updateSubject(id, name));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSubject(@RequestParam String subject) {
        subjectService.deleteSubject(subject);
        return ResponseEntity.noContent().build();
    }
}
