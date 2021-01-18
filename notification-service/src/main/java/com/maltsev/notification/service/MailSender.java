package com.maltsev.notification.service;

import com.maltsev.cross.message.EmailMessage;

public interface MailSender {
    void sendMessage(EmailMessage message);
}
