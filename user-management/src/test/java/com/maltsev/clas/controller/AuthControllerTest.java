package com.maltsev.clas.controller;

import com.maltsev.clas.controller.advice.WebExceptionHandler;
import com.maltsev.clas.controller.constants.URIConstants;
import com.maltsev.clas.entity.UserEntity;
import com.maltsev.clas.exception.ChangePasswordException;
import com.maltsev.clas.request.LoginRequest;
import com.maltsev.clas.request.PasswordRequest;
import com.maltsev.clas.request.RegistrationRequest;
import com.maltsev.clas.response.AuthResponse;
import com.maltsev.clas.response.ChangePasswordResponse;
import com.maltsev.clas.service.UserService;
import com.maltsev.jwt.UserAccount;
import com.maltsev.jwt.resolver.TestAuthenticationArgumentResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.maltsev.clas.TestUtils.convertObjectToJsonBytes;
import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

    @MockBean
    private UserService userService;
    @Mock
    private UserAccount account;
    private MockMvc mvc;

    @Before
    public void setup() {
        TestAuthenticationArgumentResolver resolver = new TestAuthenticationArgumentResolver(account);
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService))
                .setControllerAdvice(new WebExceptionHandler())
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    public void registerUser() throws Exception {
        AuthResponse response = new AuthResponse(DEFAULT_TOKEN);
        RegistrationRequest request = RegistrationRequest.builder()
                .name(DEFAULT_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .login(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();
        when(userService.saveUser(any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.post(URIConstants.REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN));
    }

    @Test
    public void auth() throws Exception {
        AuthResponse response = new AuthResponse(DEFAULT_TOKEN);
        LoginRequest request = LoginRequest.builder()
                .login(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();
        when(userService.findByLoginAndPassword(any(), any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.post(URIConstants.AUTH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN));
    }

    @Test
    public void getProfileInfo() throws Exception {
        UserEntity response = UserEntity.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .login(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();
        when(userService.findByUserId(any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(URIConstants.PROFILE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DEFAULT_ID))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
                .andExpect(jsonPath("$.login").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    public void changePassword() throws Exception{
        ChangePasswordResponse response = new ChangePasswordResponse(RESPONSE_PASSWORD);
        PasswordRequest request = PasswordRequest.builder()
                .oldPassword(ONE)
                .firstPassword(DEFAULT_PASSWORD)
                .secondPassword(DEFAULT_PASSWORD)
                .build();
        when(userService.changePassword(any(), any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.post(URIConstants.CHANGE_PASSWORD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(RESPONSE_PASSWORD));
    }
}