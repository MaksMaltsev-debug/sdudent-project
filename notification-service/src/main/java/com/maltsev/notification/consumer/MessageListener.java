package com.maltsev.notification.consumer;

import com.maltsev.cross.message.EmailMessage;
import com.maltsev.notification.config.RabbitMQConfig;
import com.maltsev.notification.service.MailSender;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MessageListener {
    private final MailSender mailSender;

    @RabbitListener(queues = {RabbitMQConfig.NOTIFICATION_QUEUE})
    public void onMessage(final EmailMessage message) {
        mailSender.sendMessage(message);
    }
}
