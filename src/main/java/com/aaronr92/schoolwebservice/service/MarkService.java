package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.dto.MarkDTO;
import com.aaronr92.schoolwebservice.entity.Mark;
import com.aaronr92.schoolwebservice.entity.Subject;
import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.repository.MarkRepository;
import com.aaronr92.schoolwebservice.repository.SubjectRepository;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import com.aaronr92.schoolwebservice.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public void addMark(User user, MarkDTO markDTO) {
        validateTeacher(user);
        validateMark(user, markDTO);

        User student = userRepository.findUserByUsername(markDTO.getStudent()).get();
        Optional<Mark> m = markRepository.findDistinctFirstByUser(student);
        if (m.isPresent()) {
            if (m.get().getDate().equals(LocalDate.now()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "This student has already received a mark today!");
        }

        Optional<Subject> subject = subjectRepository.findSubjectByNameIgnoreCaseAndTeacher(markDTO.getSubjectName(), user.getUsername());
        if (subject.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Teacher does not teach this subject!");
        if (!subject.get().getTeacher().equals(user.getUsername())) {
            User teacher = userRepository.findUserByUsername(subject.get().getTeacher()).get();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Only %s %s can handle marks for this subject!", teacher.getName(), teacher.getLastname()));
        }

        Mark mark = Mark.builder()
                .user(userRepository.findUserByUsername(markDTO.getStudent()).get())
                .subject(subject.get())
                .mark(markDTO.getMark())
                .teacher(String.format("%s %s", user.getName(), user.getLastname()))
                .build();
        markRepository.save(mark);
    }

    public void deleteMark(User user, MarkDTO markDTO) {
        validateTeacher(user);
        validateMark(user, markDTO);

        User student = userRepository.findUserByUsername(markDTO.getStudent()).get();
        Optional<Mark> mark = markRepository.findDistinctFirstByUser(student);

        if (mark.isPresent()) {
            if (!mark.get().getDate().equals(LocalDate.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "This mark was set later than today!");
            }
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Last mark does not present!");

        markRepository.delete(mark.get());
    }

    private void validateMark(User user, MarkDTO markDTO) {
        if (!userRepository.existsUserByUsername(markDTO.getStudent()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This student does not exist!");
        if (!subjectRepository.existsByNameIgnoreCaseAndTeacher(markDTO.getSubjectName(), user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject does not exist!");

        User receiver = userRepository.findUserByUsername(markDTO.getStudent()).get();
        if (receiver.getRoles().contains(Role.ROLE_ADMINISTRATOR) ||
                receiver.getRoles().contains(Role.ROLE_CURATOR) ||
                receiver.getRoles().contains(Role.ROLE_TEACHER) ||
                !receiver.getRoles().contains(Role.ROLE_STUDENT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only student can get marks!");
        }
    }

    private void validateTeacher(User user) {
        if (!user.getRoles().contains(Role.ROLE_TEACHER))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only teacher can perform operations with marks!");
    }
}
