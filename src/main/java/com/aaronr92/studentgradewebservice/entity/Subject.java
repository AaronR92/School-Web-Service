package com.aaronr92.studentgradewebservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "subjects")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(generator = "subjectSeq")
    @SequenceGenerator(name = "subjectSeq", sequenceName = "SUBJECT_SEQ", allocationSize = 1)
    @ReadOnlyProperty
    private Long id;

    @JsonProperty("subject_name")
    private String name;

    @OneToMany
    @JsonProperty("teacher_username")
    private Set<User> teachers;

    public void addTeacher(User teacher) {
        if (teachers == null) {
            teachers = new HashSet<>();
        }
        teachers.add(teacher);
    }

    public void removeTeacher(User teacher) {
        teachers.remove(teacher);
    }
}
