package com.maltsev.clas.controller;

import com.maltsev.clas.constants.URIConstants;
import com.maltsev.clas.controller.advice.WebExceptionHandler;
import com.maltsev.clas.request.SetNotificationsRequest;
import com.maltsev.clas.response.DeleteUserResponse;
import com.maltsev.clas.response.NotificationsResponse;
import com.maltsev.clas.response.UserResponse;
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

import java.util.List;

import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static com.maltsev.clas.service.impl.util.TestUtils.convertObjectToJsonBytes;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @MockBean
    private UserService userService;

    private MockMvc mvc;

    @Mock
    private UserAccount account;

    @Before
    public void setup() {
        TestAuthenticationArgumentResolver resolver = new TestAuthenticationArgumentResolver(account);
        mvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new WebExceptionHandler())
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    public void getAllUsers() throws Exception {
        List<UserResponse> response = List.of(getDefaultUserInfoResponse(ONE), getDefaultUserInfoResponse(TWO), getDefaultUserInfoResponse(THREE));
        when(userService.getAllUsers()).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(URIConstants.ALL_USERS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(ONE))
                .andExpect(jsonPath("$.[1].id").value(TWO))
                .andExpect(jsonPath("$.[2].id").value(THREE));

    }

    @Test
    public void deleteUser() throws Exception {
        DeleteUserResponse response = DeleteUserResponse.builder()
                .result(RESPONSE_DELETE_USER)
                .build();
        when(userService.deleteUserById(any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.delete(URIConstants.USER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(RESPONSE_DELETE_USER));
    }

    @Test
    public void setNotifications() throws Exception {
        NotificationsResponse response = NotificationsResponse.builder()
                .activityInClasses(true)
                .informationAboutUpdates(true)
                .notificationClassStart(true)
                .build();
        SetNotificationsRequest request = SetNotificationsRequest.builder()
                .activityInClasses(true)
                .informationAboutUpdates(true)
                .notificationClassStart(true)
                .build();
        when(userService.setNotifications(any(), any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.post(URIConstants.NOTIFICATIONS)
                .header(AUTHORIZATION, DEFAULT_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityInClasses").value(true))
                .andExpect(jsonPath("$.notificationClassStart").value(true))
                .andExpect(jsonPath("$.informationAboutUpdates").value(true));
    }

    @Test
    public void getNotifications() throws Exception {
        NotificationsResponse response = NotificationsResponse.builder()
                .activityInClasses(true)
                .informationAboutUpdates(true)
                .notificationClassStart(true)
                .build();
        when(userService.getNotifications(any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(URIConstants.NOTIFICATIONS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityInClasses").value(true))
                .andExpect(jsonPath("$.notificationClassStart").value(true))
                .andExpect(jsonPath("$.informationAboutUpdates").value(true));
    }

    UserResponse getDefaultUserInfoResponse(String userId) {
        return UserResponse.builder()
                .id(userId)
                .name(DEFAULT_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .build();

    }
}