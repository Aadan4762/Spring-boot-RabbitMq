package com.example.springbootrabbitMQ.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    @Value("${rabbitmq.queue.json.name}")
    private String jsonQueue;
    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    //spring bean for rabbitMQ queue
    @Bean
    public Queue queue(){
        return new Queue(queue);
    }

    //spring bean for Queue to store JSON message
    @Bean
    public Queue jsonQueue(){
        return new Queue(jsonQueue);
    }
    //spring bean for rabbitMQ exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    //Binding between Queue and  exchange using Routing key
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }
    //binding our json Queue to exchange using routingKey
    @Bean
    public Binding jsonBinding(){
        return BindingBuilder
                .bind(jsonQueue())
                .to(exchange())
                .with(routingJsonKey);
    }
    //creating message converter
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
    //rabbitMq template creation and setting message converter to it
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate =  new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
