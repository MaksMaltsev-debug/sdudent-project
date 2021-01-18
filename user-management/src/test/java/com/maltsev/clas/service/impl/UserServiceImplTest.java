package com.maltsev.clas.service.impl;

import com.maltsev.clas.entity.UserEntity;
import com.maltsev.clas.exception.ChangePasswordException;
import com.maltsev.clas.exception.UserNotFoundException;
import com.maltsev.clas.repository.UserRepository;
import com.maltsev.clas.request.PasswordRequest;
import com.maltsev.clas.request.RegistrationRequest;
import com.maltsev.clas.response.AuthResponse;
import com.maltsev.clas.service.UserService;
import com.maltsev.jwt.UserAccount;
import com.maltsev.jwt.provider.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @MockBean
    private JwtTokenProvider provider;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private RabbitTemplate rabbitTemplate;
    UserService userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(provider, userRepository, passwordEncoder, rabbitTemplate);
    }

    @Test
    public void saveUser_WhenRequestIsValid_ExceptAuthResponse() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .login(DEFAULT_EMAIL)
                .name(DEFAULT_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .password(DEFAULT_PASSWORD)
                .build();
        when(passwordEncoder.encode(any())).thenReturn(DEFAULT_PASSWORD);
        when(userRepository.save(any())).thenReturn(new UserEntity());
        userService.saveUser(registrationRequest);
        verify(userRepository, times(1)).save(any());
        verify(provider, times(1)).generateToken(any());
    }

    @Test
    public void findByUserId_WhenRequestIsValid_ExceptUserEntity() {
        when(userRepository.findById(any())).thenReturn(Optional.of(getUserWithId(DEFAULT_ID)));
        UserEntity userEntity = userService.findByUserId(DEFAULT_ID);
        assertEquals(DEFAULT_ID, userEntity.getId());
    }

    @Test
    public void findByLoginAndPassword_WhenRequestIsValid_ExceptUserEntity() {
        when(userRepository.findById(any())).thenReturn(Optional.of(getUserWithId(DEFAULT_ID)));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(provider.generateToken(any())).thenReturn(DEFAULT_TEXT);
        AuthResponse response = userService.findByLoginAndPassword(DEFAULT_ID, DEFAULT_PASSWORD);
        assertEquals(DEFAULT_TEXT, response.getToken());
    }

    @Test(expected = UserNotFoundException.class)
    public void findByLoginAndPassword_WhenUserIsEmpty_ExceptUserNotFoundException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(provider.generateToken(any())).thenReturn(DEFAULT_TEXT);
        AuthResponse response = userService.findByLoginAndPassword(DEFAULT_ID, DEFAULT_PASSWORD);
        assertNull(response);
    }

    @Test
    public void changePassword_WhenRequestIsValid_ExpectMethodSave() {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        PasswordRequest request = PasswordRequest.builder()
                .oldPassword(DEFAULT_PASSWORD)
                .firstPassword(DEFAULT_PASSWORD)
                .secondPassword(DEFAULT_PASSWORD)
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(getUserWithId(DEFAULT_ID)));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        userService.changePassword(account, request);
        verify(userRepository, times(1)).save(any());
    }

    @Test(expected = ChangePasswordException.class)
    public void changePassword_WhenOldPasswordIsNotValid_ExpectChangePasswordException() {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        PasswordRequest request = PasswordRequest.builder()
                .oldPassword(DEFAULT_PASSWORD)
                .firstPassword(DEFAULT_PASSWORD)
                .secondPassword(DEFAULT_PASSWORD)
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(getUserWithId(DEFAULT_ID)));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        userService.changePassword(account, request);
    }

    @Test(expected = ChangePasswordException.class)
    public void changePassword_WhenNewPasswordsAreNotEqual_ExpectChangePasswordException() {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        PasswordRequest request = PasswordRequest.builder()
                .oldPassword(DEFAULT_PASSWORD)
                .firstPassword(DEFAULT_TEXT)
                .secondPassword(DEFAULT_PASSWORD)
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(getUserWithId(DEFAULT_ID)));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        userService.changePassword(account, request);
    }

    private UserEntity getUserWithId(String userId) {
        return UserEntity.builder()
                .id(userId)
                .password(DEFAULT_PASSWORD)
                .name(DEFAULT_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .login(DEFAULT_EMAIL)
                .build();
    }
}