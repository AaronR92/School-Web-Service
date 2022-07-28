package com.aaronr92.schoolwebservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "marks")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Mark {

    @Id
    @GeneratedValue(generator = "marksSeq")
    @SequenceGenerator(name = "marksSeq", sequenceName = "MARK_SEQ", allocationSize = 1)
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @CreationTimestamp
    private LocalDate date;

    @ManyToOne
    private Subject subject;

    private String teacher;

    private int mark;
}
