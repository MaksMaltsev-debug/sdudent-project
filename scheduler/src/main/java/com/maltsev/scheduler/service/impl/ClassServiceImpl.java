package com.maltsev.scheduler.service.impl;


import com.maltsev.cross.constatns.Status;
import com.maltsev.cross.message.ClassStatusMessage;
import com.maltsev.scheduler.config.RabbitMQConfig;
import com.maltsev.scheduler.service.ClassService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void startClass(String userId, String classId) {
        log.info("Message payload to rabbit mq: user {}, class {}", userId, classId);
        ClassStatusMessage classStatusMessage = getClassStatusMessage(userId, classId, Status.IN_PROGRESS);
        rabbitTemplate.convertAndSend(RabbitMQConfig.SCHEDULER_CHANGE_STATUS_QUEUE, classStatusMessage);
    }

    @Override
    public void endClass(String userId, String classId) {
        log.info("Message payload to rabbit mq: user {}, class {}", userId, classId);
        ClassStatusMessage classStatusMessage = getClassStatusMessage(userId, classId, Status.COMPLETED);
        rabbitTemplate.convertAndSend(RabbitMQConfig.SCHEDULER_CHANGE_STATUS_QUEUE, classStatusMessage);
    }

    private ClassStatusMessage getClassStatusMessage(String userId, String classId, Status state) {
        return ClassStatusMessage.builder()
                .userId(userId)
                .classId(classId)
                .status(state)
                .build();
    }
}
