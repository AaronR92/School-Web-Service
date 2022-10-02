package com.aaronr92.studentgradewebservice;

import com.aaronr92.studentgradewebservice.util.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestUserDTO {

    String name;
    String lastname;
    String email;
    int group;
    String password;
    String phone;
    String dob;
    Gender gender;
}
