package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.entity.Subject;
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

    public Map<String, String> addNewSubject(Subject subject) {
        if (!userRepository.existsUserByUsername(subject.getTeacher())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This teacher is not registered!");
        }
        if (!userRepository.findUserByUsername(subject.getTeacher()).get()
                .getRoles().contains(Role.ROLE_TEACHER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only teachers can lead subjects!");
        }

        subjectRepository.save(subject);
        return Map.of("status", "success!");
    }

    public void deleteSubject(String subjectName, String teacher) {
        Optional<Subject> subject = subjectRepository.findSubjectByNameIgnoreCaseAndTeacher(subjectName, teacher);
        if (subject.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject is not present!");
        subjectRepository.delete(subject.get());
    }
}
