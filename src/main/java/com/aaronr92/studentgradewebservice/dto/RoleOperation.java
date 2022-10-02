package com.aaronr92.studentgradewebservice.dto;

import com.aaronr92.studentgradewebservice.util.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class RoleOperation {

    private String username;

    private String role;

    private Action action;
}
