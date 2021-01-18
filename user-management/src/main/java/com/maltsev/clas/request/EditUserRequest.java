package com.maltsev.clas.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class EditUserRequest {
    @NotEmpty
    private String allName;

    @NotEmpty
    private String email;

}