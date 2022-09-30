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

        Optional<User> teacher = userRepository.findUserByUsername(subject.getTeacher_username());

        checkTeacher(teacher);

        Subject s = Subject.builder()
                        .name(subject.getSubject_name())
                .build();

        s.addTeacher(teacher.get());

        subjectRepository.save(s);

        return Map.of("status", "success!");
    }

    public void deleteSubject(String subjectName) {
        Subject subject = subjectRepository.findSubjectByNameIgnoreCase(subjectName);
        if (subject == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject is not present!");
        subjectRepository.delete(subject);
    }

    public Subject update(Long id, String operation, String teacher, String name) {
        if (operation != null && teacher != null) {
            return updateTeacher(id, operation, teacher);
        } else if (name != null) {
            return updateSubjectName(id, name);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public Subject updateTeacher(Long id, String operation, String teacherUsername) {
        Optional<Subject> subject = subjectRepository.findById(id);

        if (subject.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject not found!");

        Optional<User> teacher = userRepository.findUserByUsername(teacherUsername.trim());

        checkTeacher(teacher);

        switch (operation.trim()) {
            case "ADD": {
                if (subject.get().getTeachers().contains(teacher.get()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Teacher is already teaching this subject!");

                subject.get().addTeacher(teacher.get());
                return subjectRepository.save(subject.get());
            }
            case "REMOVE": {
                if (!subject.get().getTeachers().contains(teacher.get()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Teacher is not teaching this subject!");

                subject.get().removeTeacher(teacher.get());
                subjectRepository.save(subject.get());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Teacher has been removed");
            }
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not valid operation!");
        }

    }

    public Subject updateSubjectName(Long id, String name) {
        Optional<Subject> subject = subjectRepository.findById(id);

        if (subject.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Subject not found!");

        if (subjectRepository.existsByNameIgnoreCase(name.trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject already exists!");
        }

        subject.get().setName(name.trim());
        return subjectRepository.save(subject.get());
    }

    private void checkTeacher(Optional<User> teacher) {
        if (teacher.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This teacher is not registered!");
        }
        if (!teacher.get().getRoles().contains(Role.ROLE_TEACHER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only teachers can lead subjects!");
        }
    }
}
