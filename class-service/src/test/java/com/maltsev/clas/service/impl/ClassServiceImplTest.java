package com.maltsev.clas.service.impl;

import com.maltsev.clas.config.RabbitMQConfig;
import com.maltsev.clas.entity.ClassEntity;
import com.maltsev.clas.entity.UserEntity;
import com.maltsev.clas.exception.CreateClassException;
import com.maltsev.clas.repository.ClassRepository;
import com.maltsev.clas.repository.UserRepository;
import com.maltsev.clas.request.AddSubscribersRequest;
import com.maltsev.clas.request.CreateClassRequest;
import com.maltsev.clas.response.ClassInfoResponse;
import com.maltsev.clas.response.UserResponse;
import com.maltsev.clas.service.ClassService;
import com.maltsev.clas.service.UserService;
import com.maltsev.cross.constatns.Status;
import com.maltsev.cross.message.ClassStatusMessage;
import com.maltsev.cross.message.EmailMessage;
import com.maltsev.cross.request.JobRequest;
import com.maltsev.jwt.UserAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static com.maltsev.clas.service.impl.util.TestUtils.buildDefaultAccount;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassServiceImplTest {
    @MockBean
    private ClassRepository classRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private RabbitTemplate rabbitTemplate;
    @Mock
    private UserAccount userAccount;
    private ClassService classService;

//    @Before
//    public void setUp() {
//        classService = new ClassServiceImpl(userRepository, classRepository, rabbitTemplate, );
//        userAccount = buildDefaultAccount();
//    }

    @Test
    public void createClass_WhenRequestIsValid_ExpectClassInfoResponse() {
        CreateClassRequest createClassRequest = CreateClassRequest.builder()
                .name(DEFAULT_TEXT)
                .description(DEFAULT_TEXT)
                .startTime(ZonedDateTime.parse(DEFAULT_START_TIME))
                .finishTime(ZonedDateTime.parse(DEFAULT_FINISH_TIME))
                .build();
        when(userRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(getDefaultUser()));
        ClassInfoResponse response = classService.createClass(userAccount, createClassRequest);
        assertEquals(DEFAULT_TEXT, response.getName());
        assertEquals(DEFAULT_TEXT, response.getDescription());
        assertEquals(ZonedDateTime.parse(DEFAULT_START_TIME), response.getStartTime());
        assertEquals(ZonedDateTime.parse(DEFAULT_FINISH_TIME), response.getFinishTime());
        assertEquals(DEFAULT_ID, response.getCreator().getId());
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.SCHEDULER_QUEUE), any(JobRequest.class));
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.NOTIFICATION_QUEUE), any(EmailMessage.class));
    }

    @Test(expected = CreateClassException.class)
    public void createClass_WhenTimeIsNull_ExpectCreateClassException() {
        CreateClassRequest createClassRequest = CreateClassRequest.builder()
                .name(DEFAULT_TEXT)
                .description(DEFAULT_TEXT)
                .startTime(null)
                .finishTime(null)
                .build();
        UserEntity userEntity = getDefaultUser();
        when(userRepository.findById(userAccount.getUserId())).thenReturn(java.util.Optional.ofNullable(userEntity));
        classService.createClass(userAccount, createClassRequest);
    }

    @Test(expected = CreateClassException.class)
    public void createClass_WhenTimeIsBeforeNow_ExpectCreateClassException() {
        CreateClassRequest createClassRequest = CreateClassRequest.builder()
                .name(DEFAULT_TEXT)
                .description(DEFAULT_TEXT)
                .startTime(ZonedDateTime.parse(INVALID_START_TIME))
                .finishTime(ZonedDateTime.parse(INVALID_FINISH_TIME))
                .build();
        UserEntity userEntity = UserEntity.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_TEXT)
                .lastName(DEFAULT_TEXT)
                .email(DEFAULT_EMAIL)
                .build();
        when(userRepository.findById(userAccount.getUserId())).thenReturn(java.util.Optional.ofNullable(userEntity));
        ClassInfoResponse response = classService.createClass(userAccount, createClassRequest);
    }

    @Test(expected = CreateClassException.class)
    public void createClass_WhenStartBeforeFinish_ExpectCreateClassException() {
        CreateClassRequest createClassRequest = CreateClassRequest.builder()
                .name(DEFAULT_TEXT)
                .description(DEFAULT_TEXT)
                .startTime(ZonedDateTime.parse(DEFAULT_FINISH_TIME))
                .finishTime(ZonedDateTime.parse(DEFAULT_START_TIME))
                .build();
        UserEntity userEntity = UserEntity.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_TEXT)
                .lastName(DEFAULT_TEXT)
                .email(DEFAULT_EMAIL)
                .build();
        when(userRepository.findById(userAccount.getUserId())).thenReturn(java.util.Optional.ofNullable(userEntity));
        ClassInfoResponse response = classService.createClass(userAccount, createClassRequest);
    }

    @Test
    public void getClassInfo_WhenRequestIsValid_ExpectClassInfoResponse() {
        ClassEntity classEntity = ClassEntity.builder()
                .name(DEFAULT_TEXT)
                .description(DEFAULT_TEXT)
                .id(DEFAULT_ID)
                .startTime(ZonedDateTime.parse(DEFAULT_START_TIME))
                .finishTime(ZonedDateTime.parse(DEFAULT_FINISH_TIME))
                .creator(getDefaultUser())
                .build();
        when(classRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(classEntity));
        ClassInfoResponse response = classService.getClassInfo(classEntity.getId());
        assertEquals(classEntity.getName(), response.getName());
        assertEquals(classEntity.getDescription(), response.getDescription());
        assertEquals(classEntity.getStartTime(), response.getStartTime());
        assertEquals(classEntity.getFinishTime(), response.getFinishTime());
        assertEquals(classEntity.getCreator().getId(), response.getCreator().getId());
    }

    @Test
    public void getClassInfo_WhenClassDoesNotExist_ExpectNull() {
        when(classRepository.findById(any())).thenReturn(Optional.empty());
        assertNull(classService.getClassInfo(DEFAULT_ID));
    }


    @Test
    public void getAllClass_WhenListIsNotEmpty_ExpectClassInfoResponseList() {
        List<ClassEntity> classEntityList = List.of(getClassWithId(ONE), getClassWithId(TWO), getClassWithId(THREE));
        when(classRepository.findAll()).thenReturn(classEntityList);
        List<ClassInfoResponse> responsesList = classService.getAllClass();
        assertEquals(classEntityList.size(), responsesList.size());
        assertEquals(ONE, responsesList.get(0).getId());
        assertEquals(TWO, responsesList.get(1).getId());
        assertEquals(THREE, responsesList.get(2).getId());

    }

    @Test
    public void getMyClass_WhenRequestIsValid_ExpectClassInfoResponseList() {
        List<ClassEntity> classEntityList = List.of(getClassWithId(ONE), getClassWithId(TWO), getClassWithId(THREE));
        when(classRepository.findAllByCreatorId(DEFAULT_ID)).thenReturn(classEntityList);
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        List<ClassInfoResponse> responsesList = classService.getMyClass(account);
        assertEquals(classEntityList.size(), responsesList.size());
        assertEquals(ONE, responsesList.get(0).getId());
        assertEquals(TWO, responsesList.get(1).getId());
        assertEquals(THREE, responsesList.get(2).getId());
    }

    @Test
    public void getMyClassSubscribers_WhenRequestIsValid_ExpectUserResponseList() {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        List<ClassEntity> classEntityList = List.of(getClassWithId(ONE), getClassWithId(TWO), getClassWithId(THREE));
        when(classRepository.findAllByCreatorId(DEFAULT_ID)).thenReturn(classEntityList);
        List<UserResponse> response = classService.getMyClassSubscribers(account);
        assertEquals(NINE, response.size());
        assertEquals(ONE, response.get(0).getId());
        assertEquals(TWO, response.get(1).getId());
        assertEquals(THREE, response.get(2).getId());

    }

    @Test
    public void addSubscribers_WhenRequestIsValid_ExpectAddSubscribersResponse() {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        AddSubscribersRequest request = new AddSubscribersRequest(DEFAULT_ID);
        when(classRepository.findById(any())).thenReturn(Optional.ofNullable(getDefaultClass()));
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(getDefaultUser()));
        classService.addSubscribers(account, request);
        verify(classRepository, times(1)).save(any());
    }

    @Test
    public void setStatus_WhenStatusWait_ExpectClassEntityWithNewStatus() {
        ClassStatusMessage message = ClassStatusMessage.builder()
                .userId(DEFAULT_ID)
                .classId(ONE)
                .status(Status.IN_PROGRESS)
                .build();
        ClassEntity classEntity = getClassWithId(ONE);
        when(classRepository.findById(ONE)).thenReturn(Optional.ofNullable(classEntity));
        classService.setStatus(message);
        verify(classRepository, times(1)).save(classEntity);
    }

    private UserEntity getDefaultUser() {
        return UserEntity.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_TEXT)
                .lastName(DEFAULT_TEXT)
                .email(DEFAULT_EMAIL)
                .build();
    }

    private UserEntity getUserWithId(String userId) {
        return UserEntity.builder()
                .id(userId)
                .firstName(DEFAULT_TEXT)
                .lastName(DEFAULT_TEXT)
                .email(DEFAULT_EMAIL)
                .build();
    }

    private ClassEntity getDefaultClass() {
        return ClassEntity.builder()
                .name(DEFAULT_TEXT)
                .description(DEFAULT_TEXT)
                .id(DEFAULT_ID)
                .startTime(ZonedDateTime.parse(DEFAULT_START_TIME))
                .finishTime(ZonedDateTime.parse(DEFAULT_FINISH_TIME))
                .creator(getDefaultUser())
                .subscribers(new ArrayList<>())
                .build();
    }

    private ClassEntity getClassWithId(String classId) {
        List<UserEntity> subscribersList = List.of(getUserWithId(ONE), getUserWithId(TWO), getUserWithId(THREE));
        return ClassEntity.builder()
                .name(DEFAULT_TEXT)
                .description(DEFAULT_TEXT)
                .id(classId)
                .startTime(ZonedDateTime.parse(DEFAULT_START_TIME))
                .finishTime(ZonedDateTime.parse(DEFAULT_FINISH_TIME))
                .creator(getDefaultUser())
                .subscribers(subscribersList)
                .build();
    }

}