package com.maltsev.notification.service;

import com.maltsev.cross.message.EmailMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;


import javax.mail.internet.MimeMessage;

import static com.maltsev.notification.service.TestConstants.DEFAULT_EMAIL;
import static com.maltsev.notification.service.TestConstants.DEFAULT_TEXT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
public class MailSenderImplTest {
    @Mock
    JavaMailSender mailSender;
    private MailSender mailSenderImpl;

    @Before
    public void setUp() {
        mailSenderImpl = new MailSenderImpl(mailSender);
    }

    @Test
    public void send() {
        EmailMessage emailMessage = EmailMessage.builder()
                .email(DEFAULT_EMAIL)
                .body(DEFAULT_TEXT)
                .topic(DEFAULT_TEXT)
                .build();
        mailSenderImpl.sendMessage(emailMessage);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}