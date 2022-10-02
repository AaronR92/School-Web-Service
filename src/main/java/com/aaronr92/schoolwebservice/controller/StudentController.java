package com.aaronr92.schoolwebservice.controller;

import com.aaronr92.schoolwebservice.dto.MarkDTO;
import com.aaronr92.schoolwebservice.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final MarkService markService;

    @PostMapping("/mark")
    public ResponseEntity<Map<String, String>> addMark(Principal principal,
                                                       @RequestBody MarkDTO markDTO) {
        markService.addMark(principal, markDTO);
        return ResponseEntity.ok().body(Map.of("status", "success"));
    }

    @DeleteMapping("/mark")
    public ResponseEntity<Void> deleteMark(Principal principal,
                                           @RequestBody MarkDTO markDTO) {
        markService.deleteMark(principal, markDTO);
        return ResponseEntity.noContent().build();
    }
}
