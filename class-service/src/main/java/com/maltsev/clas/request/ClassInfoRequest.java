package com.maltsev.clas.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ClassInfoRequest {
    @NotEmpty
    private String id;

}
