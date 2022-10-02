package com.aaronr92.studentgradewebservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDTO {

    @NotBlank
    private String subject_name;

    @NotBlank
    private String teacher_username;
}
