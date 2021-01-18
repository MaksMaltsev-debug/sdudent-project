package com.maltsev.clas.rabbit.consumer;

import com.maltsev.clas.repository.ClassRepository;
import com.maltsev.clas.repository.UserRepository;
import com.maltsev.clas.service.ClassService;
import com.maltsev.clas.service.UserService;
import com.maltsev.clas.service.impl.ClassServiceImpl;
import com.maltsev.clas.service.impl.UserServiceImpl;
import com.maltsev.cross.constatns.Status;
import com.maltsev.cross.message.ClassStatusMessage;
import com.maltsev.cross.message.NewUserMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static com.maltsev.clas.service.impl.util.TestUtils.buildDefaultAccount;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageListenerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private ClassService classService;
    private MessageListener messageListener;
    @Before
    public void setUp() {
        messageListener = new MessageListener(userService, classService);
    }

    @Test
    public void onMessage() {
        NewUserMessage message = NewUserMessage.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .build();
        messageListener.onMessage(message);
        verify(userService, times(1)).createUser(any());

    }

    @Test
    public void OnMessageScheduler() {
        ClassStatusMessage message = ClassStatusMessage.builder()
                .status(Status.IN_PROGRESS)
                .classId(DEFAULT_ID)
                .userId(DEFAULT_ID)
                .build();
        messageListener.onMessageScheduler(message);
        verify(classService, times(1)).setStatus(any());
    }
}