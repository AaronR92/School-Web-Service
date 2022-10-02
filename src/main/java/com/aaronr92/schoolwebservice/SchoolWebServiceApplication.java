package com.aaronr92.schoolwebservice;

import com.aaronr92.schoolwebservice.entity.Group;
import com.aaronr92.schoolwebservice.repository.GroupRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SchoolWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolWebServiceApplication.class, args);
    }


    @Bean
    BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner runner(GroupRepository groupRepository) {
        return args -> {
            if (groupRepository.count() == 0) {
                groupRepository.save(Group.builder()
                        .groupName("Staff")
                        .groupNumber(0)
                        .build());
            }
        };
    }
}
