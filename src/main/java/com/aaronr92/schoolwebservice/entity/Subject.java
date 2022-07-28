package com.aaronr92.schoolwebservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

@Entity(name = "subjects")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(generator = "subjectSeq")
    @SequenceGenerator(name = "subjectSeq", sequenceName = "SUBJECT_SEQ", allocationSize = 1)
    @JsonIgnore
    private Long id;

    @JsonProperty("subject_name")
    @NotBlank
    private String name;

    @JsonProperty("teacher_username")
    @NotBlank
    private String teacher;
}
