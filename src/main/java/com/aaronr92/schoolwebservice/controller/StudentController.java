package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.dto.MarkDTO;
import com.aaronr92.schoolwebservice.entity.User;
import com.aaronr92.schoolwebservice.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final MarkService markService;

    @PostMapping("/mark")
    public ResponseEntity<Map<String, String>> addMark(@AuthenticationPrincipal User user,
                                                @RequestBody MarkDTO markDTO) {
        markService.addMark(user, markDTO);
        return ResponseEntity.ok().body(Map.of("status", "success"));
    }

    @DeleteMapping("/mark")
    public ResponseEntity<?> deleteMark(@AuthenticationPrincipal User user,
                    @RequestBody MarkDTO markDTO) {
        markService.deleteMark(user, markDTO);
        return ResponseEntity.noContent().build();
    }
}
