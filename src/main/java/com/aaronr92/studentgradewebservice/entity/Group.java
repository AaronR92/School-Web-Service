package com.aaronr92.studentgradewebservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "s_group")
public class Group {

    @Id
    @GeneratedValue(generator = "groupSeq")
    @SequenceGenerator(name = "groupSeq", sequenceName = "GROUP_SEQ", allocationSize = 1)
    @ReadOnlyProperty
    private Long id;

    @JsonProperty("group_number")
    @NotNull
    private Integer groupNumber;

    @JsonProperty("group_name")
    @NotBlank
    private String groupName;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    @ReadOnlyProperty
    private List<User> users;
}
