package com.aaronr92.schoolwebservice;

import com.aaronr92.schoolwebservice.util.Gender;
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
    int number_by_order;
    String password;
    String phone;
    String dob;
    Gender gender;
}
