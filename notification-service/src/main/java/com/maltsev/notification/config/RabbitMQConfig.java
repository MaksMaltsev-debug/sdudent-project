package com.maltsev.notification.config;

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

    public static final String NOTIFICATION_QUEUE = "notification";
    public static final String DEAD_LETTER_QUEUE = "notification.dead-letter-queue";

    private static final String DEAD_LETTER_EXCHANGE_ARG = "x-dead-letter-exchange";
    private static final String DEAD_LETTER_ROUTING_KEY_ARG = "x-dead-letter-routing-key";

    private static final String EXCHANGE = "exchange";
    private static final String BINDING_KEY = "bindingKey";

    @Bean
    public Exchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument(DEAD_LETTER_EXCHANGE_ARG, "")
                .withArgument(DEAD_LETTER_ROUTING_KEY_ARG, DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    Binding binding() {
        return BindingBuilder
                .bind(notificationsQueue())
                .to(exchange())
                .with(BINDING_KEY)
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
