package com.aaronr92.schoolwebservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;

@Getter @Setter
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
