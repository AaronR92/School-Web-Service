package com.aaronr92.schoolwebservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {

    @NotBlank
    private String subject_name;

    @NotBlank
    private String teacher_username;
}
