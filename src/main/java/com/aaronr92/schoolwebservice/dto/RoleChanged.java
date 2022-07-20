package com.aaronr92.schoolwebservice.dto;

import com.aaronr92.schoolwebservice.util.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class RoleChanged {

    private String username;

    private String role;

    private Action action;
}
