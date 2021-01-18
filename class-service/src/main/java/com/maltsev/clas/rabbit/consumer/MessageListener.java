package com.maltsev.clas.rabbit.consumer;

import com.maltsev.clas.service.ClassService;
import com.maltsev.cross.constatns.Status;
import com.maltsev.cross.message.ClassStatusMessage;
import com.maltsev.cross.message.NewUserMessage;
import com.maltsev.clas.config.RabbitMQConfig;
import com.maltsev.clas.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MessageListener {
    private final UserService userService;

    private final ClassService classService;

    @RabbitListener(queues = {RabbitMQConfig.NEW_USER_QUEUE})
    public void onMessage(final NewUserMessage message) {
        userService.createUser(message);
    }

    @RabbitListener(queues = {RabbitMQConfig.SCHEDULER_CHANGE_STATUS_QUEUE})
    public void onMessageScheduler(final ClassStatusMessage message) {
        classService.setStatus(message);
        if (message.getStatus() == Status.IN_PROGRESS) {
            classService.startClass(message);
        }
    }

}
