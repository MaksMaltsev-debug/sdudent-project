package com.maltsev.clas.controller;

import com.maltsev.jwt.UserAccount;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@CrossOrigin("*")
public class UserConverterController {

    @GetMapping("/security/get-user-details")
    @ResponseStatus(HttpStatus.OK)
    public UserAccount getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return UserAccount.builder()
                .userId(userDetails.getUsername())
                .build();
    }

    private List<String> getRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }
}
