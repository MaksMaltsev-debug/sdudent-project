package com.maltsev.clas.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@EnableRabbit
@Configuration
public class RabbitMQConfig {

    public static final String NEW_USER_QUEUE = "new.user";
    public static final String DEAD_LETTER_QUEUE = "new.user.dead-letter-queue";
    public static final String SCHEDULER_CHANGE_STATUS_QUEUE = "scheduler.change.status";
    public static final String SCHEDULER_CHANGE_STATUS_DEAD_LETTER_QUEUE = "scheduler.change.status.dead-letter-queue";

    private static final String DEAD_LETTER_EXCHANGE_ARG = "x-dead-letter-exchange";
    private static final String DEAD_LETTER_ROUTING_KEY_ARG = "x-dead-letter-routing-key";

    private static final String EXCHANGE = "exchange";
    private static final String BINDING_KEY = "bindingKey";
    private static final String SCHEDULER_CHANGE_STATUS_EXCHANGE = "scheduler.change.status.exchange";
    private static final String SCHEDULER_CHANGE_STATUS_BINDING_KEY = "scheduler.change.status.bindingKey";

    public static final String NOTIFICATION_QUEUE = "notification";
    public static final String SCHEDULER_QUEUE = "scheduler";

    @Bean
    public Exchange newUserExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue newUserQueue() {
        return QueueBuilder.durable(NEW_USER_QUEUE)
                .withArgument(DEAD_LETTER_EXCHANGE_ARG, "")
                .withArgument(DEAD_LETTER_ROUTING_KEY_ARG, DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Queue newUserDeadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    public Binding newUserBinding() {
        return BindingBuilder
                .bind(newUserQueue())
                .to(newUserExchange())
                .with(BINDING_KEY)
                .noargs();
    }

    @Bean
    public Exchange schedulerChangeStatusExchange() {
        return new TopicExchange(SCHEDULER_CHANGE_STATUS_EXCHANGE);
    }

    @Bean
    public Queue schedulerChangeStatusQueue() {
        return QueueBuilder.durable(SCHEDULER_CHANGE_STATUS_QUEUE)
                .withArgument(DEAD_LETTER_EXCHANGE_ARG, "")
                .withArgument(DEAD_LETTER_ROUTING_KEY_ARG, SCHEDULER_CHANGE_STATUS_DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Queue schedulerChangeStatusDeadLetterQueue() {
        return new Queue(SCHEDULER_CHANGE_STATUS_DEAD_LETTER_QUEUE);
    }

    @Bean
    public Binding schedulerChangeStatusBinding() {
        return BindingBuilder
                .bind(schedulerChangeStatusQueue())
                .to(schedulerChangeStatusExchange())
                .with(SCHEDULER_CHANGE_STATUS_BINDING_KEY)
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
        return new Jackson2JsonMessageConverter();
    }
}
