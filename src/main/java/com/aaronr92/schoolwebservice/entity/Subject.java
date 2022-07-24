package com.aaronr92.schoolwebservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private String name;

    private String teacher;
}
