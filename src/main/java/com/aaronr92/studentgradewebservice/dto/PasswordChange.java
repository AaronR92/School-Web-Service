package com.aaronr92.studentgradewebservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChange {

    @JsonProperty(value = "current_password",
            access = JsonProperty.Access.WRITE_ONLY)
    private String currentPassword;

    @JsonProperty(value = "new_password",
            access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;

    @ReadOnlyProperty
    private String status;
}
