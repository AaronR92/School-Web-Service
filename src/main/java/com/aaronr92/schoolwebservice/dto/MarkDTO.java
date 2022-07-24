package com.aaronr92.schoolwebservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkDTO {

    @JsonProperty("user")
    private String username;

    @JsonProperty("subject")
    private String subjectName;

    private int mark;
}
