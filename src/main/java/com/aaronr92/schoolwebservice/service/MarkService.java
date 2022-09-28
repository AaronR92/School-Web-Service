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
        validateMark(markDTO);

        User student = userRepository.findUserByUsername(markDTO.getStudent()).get();
        Optional<Mark> mark = markRepository.findDistinctFirstByUser(student);
        if (mark.isPresent()) {
            if (mark.get().getDate().equals(LocalDate.now()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "This student has already received a mark today!");
        }

        Subject subject = subjectRepository.findSubjectByNameIgnoreCase(markDTO.getSubjectName());
        if (!subject.getTeachers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You don't teach this subject!");
        }

        Mark m = Mark.builder()
                .user(userRepository.findUserByUsername(markDTO.getStudent()).get())
                .subject(subject)
                .mark(markDTO.getMark())
                .teacher(String.format("%s %s", user.getName(), user.getLastname()))
                .build();
        markRepository.save(m);
    }

    public void deleteMark(User user, MarkDTO markDTO) {
        validateTeacher(user);
        validateMark(markDTO);

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

    private void validateMark(MarkDTO markDTO) {
        if (!userRepository.existsUserByUsername(markDTO.getStudent()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This student does not exist!");
        if (!subjectRepository.existsByNameIgnoreCase(markDTO.getSubjectName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject does not exist!");

        User receiver = userRepository.findUserByUsername(markDTO.getStudent()).get();
        if (receiver.getRoles().contains(Role.ROLE_ADMINISTRATOR) ||
                receiver.getRoles().contains(Role.ROLE_TEACHER) ||
                !receiver.getRoles().contains(Role.ROLE_STUDENT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only students can get marks!");
        }
    }

    private void validateTeacher(User user) {
        if (!user.getRoles().contains(Role.ROLE_TEACHER))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only teacher can perform operations with marks!");
    }
}
