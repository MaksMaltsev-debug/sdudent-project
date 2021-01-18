package com.maltsev.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserAccount {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("accessToken")
    private String accessToken;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class NotPermitted extends RuntimeException {
    }
}
