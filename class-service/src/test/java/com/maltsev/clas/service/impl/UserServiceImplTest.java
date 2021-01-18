package com.maltsev.clas.service.impl;

import com.maltsev.clas.entity.UserEntity;
import com.maltsev.clas.entity.status.StatusEnum;
import com.maltsev.clas.repository.UserRepository;
import com.maltsev.clas.request.SetNotificationsRequest;
import com.maltsev.clas.response.NotificationsResponse;
import com.maltsev.clas.response.UserResponse;
import com.maltsev.clas.service.UserService;
import com.maltsev.cross.message.NewUserMessage;
import com.maltsev.jwt.UserAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;
    private UserService userService;

//    @Before
//    public void setUp() {
//        userService = new UserServiceImpl(userRepository);
//    }

    @Test
    public void createUser_WhenRequestIsValid_ExpectUserEntity() {
        NewUserMessage message = NewUserMessage.builder()
                .id(DEFAULT_ID)
                .email(DEFAULT_EMAIL)
                .firstName(DEFAULT_TEXT)
                .lastName(DEFAULT_TEXT)
                .build();
        UserResponse response = userService.createUser(message);
        assertEquals(DEFAULT_TEXT, response.getName());
        assertEquals(DEFAULT_ID, response.getId());
        assertEquals(DEFAULT_TEXT, response.getLastName());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void getAllUsers_WhenRequestIsValid_ExpectListUserEntity() {
        List<UserEntity> usersList = List.of(getUserWithId(ONE), getUserWithId(TWO), getUserWithId(THREE));
        when(userRepository.findAll()).thenReturn(usersList);
        List<UserResponse> responses = userService.getAllUsers();
        assertEquals(ONE, responses.get(0).getId());
        assertEquals(TWO, responses.get(1).getId());
        assertEquals(THREE, responses.get(2).getId());
    }

    @Test
    public void deleteUserById_WhenRequestIsValid_ExpectUserEntityWithNewStatus() {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        UserEntity userEntity = getUserWithId(ONE);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(userEntity));
        userService.deleteUserById(account);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void setNotifications_WhenRequestIsValid_ExpectNotificationsResponse() {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        SetNotificationsRequest request = SetNotificationsRequest.builder()
                .activityInClasses(true)
                .informationAboutUpdates(true)
                .notificationClassStart(true)
                .build();
        UserEntity userEntity = getUserWithId(ONE);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(userEntity));
        NotificationsResponse response = userService.setNotifications(account, request);
        verify(userRepository, times(1)).save(any());
        assertTrue(response.isActivityInClasses());
        assertTrue(response.isInformationAboutUpdates());
        assertTrue(response.isNotificationClassStart());
    }

    @Test
    public void getNotifications_WhenRequestIsValid_ExpectNotificationsResponse() {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        UserEntity userEntity = getUserWithId(ONE);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(userEntity));
        NotificationsResponse response = userService.getNotifications(account);
        assertFalse(response.isActivityInClasses());
        assertFalse(response.isInformationAboutUpdates());
        assertFalse(response.isNotificationClassStart());
    }

    private UserEntity getUserWithId(String userId) {
        return UserEntity.builder()
                .id(userId)
                .firstName(DEFAULT_TEXT)
                .lastName(DEFAULT_TEXT)
                .email(DEFAULT_EMAIL)
                .status(StatusEnum.ACTIVE)
                .activityInClasses(false)
                .informationAboutUpdates(false)
                .notificationClassStart(false)
                .build();
    }
}