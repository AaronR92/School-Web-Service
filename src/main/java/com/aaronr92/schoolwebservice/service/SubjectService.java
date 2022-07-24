package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.entity.Subject;
import com.aaronr92.schoolwebservice.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public Map<String, String> addNewSubject(Subject subject) {
        subjectRepository.save(subject);
        return Map.of("status", "success!");
    }

    public void deleteSubject(String name) {
        if (!subjectRepository.existsByNameIgnoreCase(name))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject is not present!");
        subjectRepository.delete(subjectRepository.findSubjectByNameIgnoreCase(name));
    }
}
