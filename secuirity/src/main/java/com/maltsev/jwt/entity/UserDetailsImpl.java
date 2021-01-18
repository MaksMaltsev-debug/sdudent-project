package com.maltsev.jwt.entity;

import com.maltsev.jwt.UserAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.maltsev.jwt.constants.SecurityConstants.AUTHORIZATION;
import static com.maltsev.jwt.constants.SecurityConstants.BEARER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Value("${management.url}")
    private String managementUrl;

    @Override
    public UserDetails loadUserByUsername(String token) {
        UserAccount user = getUserFromManagementService("http://localhost:8090/user-management" + "/security/get-user-details", token);
        user.setAccessToken(token);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserId())
                .password("")
                .authorities(new SimpleGrantedAuthority("USER"))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private UserAccount getUserFromManagementService(String uri, String token) {
        HttpEntity<UserAccount> requestEntity = new HttpEntity<>(null, getHttpHeaders(token));

        ResponseEntity<UserAccount> result = new RestTemplate().exchange(uri, HttpMethod.GET, requestEntity, UserAccount.class);
        return result.getBody();
    }

    private HttpHeaders getHttpHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.set(AUTHORIZATION, BEARER + token);
        return headers;
    }
}
