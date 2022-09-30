package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.dto.SubjectDTO;
import com.aaronr92.schoolwebservice.entity.Subject;
import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.repository.SubjectRepository;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import com.aaronr92.schoolwebservice.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public Map<String, String> addNewSubject(SubjectDTO subject) {

        if (subjectRepository.existsByNameIgnoreCase(subject.getSubject_name()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This subject already exists!");

        subjectRepository.save(Subject.builder()
                .name(subject.getSubject_name())
                .build());
        return Map.of("status", "success!");
    }

    public void deleteSubject(String subjectName) {
        Subject subject = subjectRepository.findSubjectByNameIgnoreCase(subjectName);
        if (subject == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject is not present!");
        subjectRepository.delete(subject);
    }

    public Subject updateTeacher(Long id, String operation, String teacher) {
        Optional<Subject> subject = subjectRepository.findById(id);

        if (subject.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject not found!");

        Optional<User> user = userRepository.findUserByUsername(teacher);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This teacher is not registered!");
        }
        if (!user.get().getRoles().contains(Role.ROLE_TEACHER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only teachers can lead subjects!");
        }

        switch (operation) {
            case "ADD": {
                subject.get().addTeacher(user.get());
                return subjectRepository.save(subject.get());
            }
            case "REMOVE": {
                subject.get().removeTeacher(user.get());
                return subjectRepository.save(subject.get());
            }
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not valid operation!");
        }

    }

    public Subject updateSubject(Long id, String name) {
        Optional<Subject> subject = subjectRepository.findById(id);

        if (subject.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Subject not found!");

        if (subjectRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject already exists!");
        }

        subject.get().setName(name);
        return subjectRepository.save(subject.get());
    }
}
