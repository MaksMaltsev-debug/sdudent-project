package com.maltsev.scheduler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Getter
@Setter
@EnableRabbit
@Configuration
public class RabbitMQConfig {

    public static final String SCHEDULER_QUEUE = "scheduler";
    public static final String SCHEDULER_DEAD_LETTER_QUEUE = "scheduler.dead-letter-queue";

    private static final String DEAD_LETTER_EXCHANGE_ARG = "x-dead-letter-exchange";
    private static final String DEAD_LETTER_ROUTING_KEY_ARG = "x-dead-letter-routing-key";

    private static final String schedulerExchange = "scheduler";
    private static final String schedulerBindingKey = "scheduler.binding.key";

    public static final String SCHEDULER_CHANGE_STATUS_QUEUE = "scheduler.change.status";

    @Bean
    public Exchange schedulerExchange() {
        return new TopicExchange(schedulerExchange);
    }

    @Bean
    public Queue schedulerQueue() {
        return QueueBuilder.durable(SCHEDULER_QUEUE)
                .withArgument(DEAD_LETTER_EXCHANGE_ARG, "")
                .withArgument(DEAD_LETTER_ROUTING_KEY_ARG, SCHEDULER_DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Queue schedulerDeadLetterQueue() {
        return new Queue(SCHEDULER_DEAD_LETTER_QUEUE);
    }

    @Bean
    public Binding schedulerBinding() {
        return BindingBuilder
                .bind(schedulerQueue())
                .to(schedulerExchange())
                .with(schedulerBindingKey)
                .noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .build();
        return new Jackson2JsonMessageConverter(objectMapper, "*");
    }
}
