package com.aaronr92.schoolwebservice.entity;

import com.aaronr92.schoolwebservice.util.Gender;
import com.aaronr92.schoolwebservice.util.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.SortNatural;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "userSeq")
    @SequenceGenerator(name = "userSeq", sequenceName = "USER_SEQ", allocationSize = 1)
    @ReadOnlyProperty
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    @Pattern(regexp = ".+@.+\\..{2,4}$")
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    @Transient
    @ReadOnlyProperty
    private int age;    // will not create 'age' column

    @JsonIgnore
    private boolean isUsernameChanged;

    @JsonManagedReference
    @JoinColumn(name = "user_id")
    @OneToMany
    private Set<Mark> marks;

    @ReadOnlyProperty
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @SortNatural
    private Set<Role> roles;

    @JsonIgnore
    private boolean isNonLocked;

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return authorities;
    }

    @JsonIgnore
    public void grantAuthority(Role role) {
        if (roles == null)
            roles = new TreeSet<>();
        roles.add(role);
    }

    @JsonIgnore
    public void removeAuthority(Role role) {
        roles.remove(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
