package com.maltsev.clas.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllClassInfoResponse {
    List<String> allClass;
}
