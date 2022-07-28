package com.aaronr92.schoolwebservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkDTO {

    @JsonProperty("student_username")
    @NotBlank
    private String student;

    @JsonProperty("subject")
    @NotBlank
    private String subjectName;

    @NotNull
    private int mark;
}
