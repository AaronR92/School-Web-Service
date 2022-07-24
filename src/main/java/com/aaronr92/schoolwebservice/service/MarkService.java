package com.aaronr92.schoolwebservice.service;

import com.aaronr92.schoolwebservice.dto.MarkDTO;
import com.aaronr92.schoolwebservice.entity.Mark;
import com.aaronr92.schoolwebservice.repository.MarkRepository;
import com.aaronr92.schoolwebservice.repository.SubjectRepository;
import com.aaronr92.schoolwebservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public void saveMark(MarkDTO markDTO) {
        if (!userRepository.existsUserByUsername(markDTO.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This user does not exist!");
        if (!subjectRepository.existsByNameIgnoreCase(markDTO.getSubjectName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This subject does not exist!");
        Mark mark = Mark.builder()
                .user(userRepository.findUserByUsername(markDTO.getUsername()).get())
                .subject(subjectRepository.findSubjectByNameIgnoreCase(markDTO.getSubjectName()))
                .mark(markDTO.getMark())
                .build();
        markRepository.save(mark);
    }
}
