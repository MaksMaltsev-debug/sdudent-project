package com.maltsev.notification.consumer;

import com.maltsev.cross.message.EmailMessage;
import com.maltsev.notification.service.MailSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.maltsev.notification.service.TestConstants.DEFAULT_EMAIL;
import static com.maltsev.notification.service.TestConstants.DEFAULT_TEXT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
public class MessageListenerTest {
    @MockBean
    private MailSender mailSender;
    private MessageListener messageListener;

    @Before
    public void setUp() {
        messageListener = new MessageListener(mailSender);
    }

    @Test
    public void onMessage() {
        EmailMessage message = EmailMessage.builder()
                .body(DEFAULT_TEXT)
                .topic(DEFAULT_TEXT)
                .email(DEFAULT_EMAIL)
                .build();
        messageListener.onMessage(message);
        verify(mailSender, times(1)).sendMessage(any());
    }
}