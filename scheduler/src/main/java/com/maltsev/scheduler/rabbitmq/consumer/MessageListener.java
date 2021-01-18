package com.maltsev.scheduler.rabbitmq.consumer;

import com.maltsev.cross.request.JobRequest;
import com.maltsev.scheduler.config.RabbitMQConfig;
import com.maltsev.scheduler.quartz.service.JobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class MessageListener {

    private final JobService jobService;

    @RabbitListener(queues = {RabbitMQConfig.SCHEDULER_QUEUE})
    public void onMessage(final JobRequest message) {
        log.info("Received message for scheduler: {}", message.getJobType());
        jobService.createJob(message);

    }
}