package com.maltsev.cross.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserMessage {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

}
