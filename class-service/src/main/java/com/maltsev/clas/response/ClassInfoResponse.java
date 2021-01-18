package com.maltsev.clas.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Builder
public class ClassInfoResponse {
    private String id;
    private String name;
    private ZonedDateTime startTime;
    private ZonedDateTime finishTime;
    private String description;
    private UserResponse creator;
    private String photo;
    private int subscribers;
}
