package com.maltsev.clas.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String photo;
    private String description;
}
