package com.maltsev.clas.entity;

import com.maltsev.clas.config.JwtConfig;
import com.maltsev.clas.exception.UserNotFoundException;
import com.maltsev.clas.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailsImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    @Override
    public UserDetails loadUserByUsername(String token) {
        String userId = getId(token);
        log.debug("Get access to user " + userId);
        final UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("USERNAME NOT FOUND: {}", userId)));

        String password = Optional.ofNullable(user.getPassword())
                .orElse("");

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId())
                .password(password)
                .authorities(new SimpleGrantedAuthority("USER"))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private String getId(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}