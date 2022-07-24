package com.aaronr92.schoolwebservice.dto;

import com.aaronr92.schoolwebservice.entity.Mark;
import com.aaronr92.schoolwebservice.util.Gender;
import com.aaronr92.schoolwebservice.util.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.SortNatural;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    private String username;

    @NotBlank
    @Pattern(regexp = "[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$")
    private String phone;

    @NotBlank
    @Pattern(regexp = ".+@.+\\..{2,4}$")
    private String email;

    @NotNull
    @JsonProperty("dob")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private String dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JsonManagedReference
    @OneToMany
    private List<Mark> marks;

    @ReadOnlyProperty
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @SortNatural
    private Set<Role> roles;

    @JsonIgnore
    private boolean isUsernameChanged;
}
