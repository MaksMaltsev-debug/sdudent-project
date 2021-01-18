package com.maltsev.clas.controller;

import com.maltsev.clas.constants.URIConstants;
import com.maltsev.clas.controller.advice.WebExceptionHandler;
import com.maltsev.clas.request.AddSubscribersRequest;
import com.maltsev.clas.request.CreateClassRequest;
import com.maltsev.clas.response.AddSubscribersResponse;
import com.maltsev.clas.response.ClassInfoResponse;
import com.maltsev.clas.response.UserResponse;
import com.maltsev.clas.service.ClassService;
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

import java.time.ZonedDateTime;
import java.util.List;

import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static com.maltsev.clas.service.impl.util.TestUtils.convertObjectToJsonBytes;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassControllerTest {
    @MockBean
    private ClassService classService;

    private MockMvc mvc;

    @Mock
    private UserAccount account;

    @Before
    public void setup() {
        TestAuthenticationArgumentResolver resolver = new TestAuthenticationArgumentResolver(account);
        mvc = MockMvcBuilders.standaloneSetup(new ClassController(classService))
                .setControllerAdvice(new WebExceptionHandler())
                .setCustomArgumentResolvers(resolver)
                .build();
    }


    @Test
    public void getClassInfo() throws Exception {
        ClassInfoResponse response = getDefaultClassInfoResponse(DEFAULT_ID);
        when(classService.getClassInfo(DEFAULT_ID)).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(URIConstants.CLASS_INFO)
                .param("id", DEFAULT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DEFAULT_ID))
                .andExpect(jsonPath("$.name").value(DEFAULT_TEXT))
                .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_MILLISECONDS))
                .andExpect(jsonPath("$.finishTime").value(DEFAULT_FINISH_TIME_MILLISECONDS))
                .andExpect(jsonPath("$.description").value(DEFAULT_TEXT));
    }

    @Test
    public void createClass() throws Exception {
        ClassInfoResponse response = getDefaultClassInfoResponse(DEFAULT_ID);
        CreateClassRequest request = CreateClassRequest.builder()
                .name(DEFAULT_TEXT)
                .description(DEFAULT_TEXT)
                .finishTime(ZonedDateTime.parse(DEFAULT_FINISH_TIME))
                .startTime(ZonedDateTime.parse(DEFAULT_START_TIME))
                .build();
        when(classService.createClass(any(), any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.post(URIConstants.CLASS_INFO)
                .header(AUTHORIZATION, DEFAULT_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DEFAULT_ID))
                .andExpect(jsonPath("$.description").value(DEFAULT_TEXT));

    }

    @Test
    public void getAllClass() throws Exception {
        List<ClassInfoResponse> response = List.of(getDefaultClassInfoResponse(ONE), getDefaultClassInfoResponse(TWO), getDefaultClassInfoResponse(THREE));
        when(classService.getAllClass()).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(URIConstants.ALL_CLASS_INFO)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(ONE))
                .andExpect(jsonPath("$.[1].id").value(TWO))
                .andExpect(jsonPath("$.[2].id").value(THREE));
    }

    @Test
    public void getMyClass() throws Exception {
        List<ClassInfoResponse> response = List.of(getDefaultClassInfoResponse(ONE), getDefaultClassInfoResponse(TWO), getDefaultClassInfoResponse(THREE));
        when(classService.getMyClass(any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(URIConstants.MY_CLASS_INFO)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(ONE))
                .andExpect(jsonPath("$.[1].id").value(TWO))
                .andExpect(jsonPath("$.[2].id").value(THREE));
    }

    @Test
    public void getMyClassSubscribers() throws Exception {
        List<UserResponse> response = List.of(getDefaultUserInfoResponse(ONE), getDefaultUserInfoResponse(TWO), getDefaultUserInfoResponse(THREE));
        when(classService.getMyClassSubscribers(any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(URIConstants.MY_CLASS_SUBSCRIBERS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(ONE))
                .andExpect(jsonPath("$.[1].id").value(TWO))
                .andExpect(jsonPath("$.[2].id").value(THREE));
    }

    @Test

    public void addSubscribers() throws Exception {
        AddSubscribersRequest request = new AddSubscribersRequest(DEFAULT_ID);
        AddSubscribersResponse response = new AddSubscribersResponse(RESPONSE_SUBSCRIBER);
        when(classService.addSubscribers(any(), any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.post(URIConstants.SUBSCRIBE)
                .header(AUTHORIZATION, DEFAULT_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(RESPONSE_SUBSCRIBER));
    }

    ClassInfoResponse getDefaultClassInfoResponse(String classId) {
        UserResponse user = UserResponse.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .build();
        return ClassInfoResponse.builder()
                .id(classId)
                .description(DEFAULT_TEXT)
                .name(DEFAULT_TEXT)
                .startTime(ZonedDateTime.parse(DEFAULT_START_TIME))
                .finishTime(ZonedDateTime.parse(DEFAULT_FINISH_TIME))
                .creator(user)
                .build();
    }

    UserResponse getDefaultUserInfoResponse(String userId) {
        return UserResponse.builder()
                .id(userId)
                .name(DEFAULT_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .build();

    }
}