package com.aaronr92.studentgradewebservice.dto;

import com.aaronr92.studentgradewebservice.util.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

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
    @Pattern(regexp = ".+@.+\\..{2,4}$")
    private String email;

    @NotNull
    private int group;

    @NotBlank
    private String password;

    @NotBlank
    @Pattern(regexp = "[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$")
    private String phone;

    @NotNull
    @JsonProperty("dob")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
