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
import java.time.LocalDate;
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
    @Pattern(regexp = ".+@.+\\..{2,4}$")
    private String email;

    @NotNull
    private int group;

    @NotNull
    @JsonProperty("number_by_order")
    private int numberByOrder;

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
