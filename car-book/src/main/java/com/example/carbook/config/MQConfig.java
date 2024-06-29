package com.example.carbook.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class MQConfig {

    @Value("${rabbitmq.message-queue}")
    private String messageQueueName;

    @Value("${rabbitmq.message-exchange}")
    private String messageExchangeName;

    @Value("${rabbitmq.mq-routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;




    @Bean
    public Queue queue(){
        return new Queue(messageQueueName);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(messageExchangeName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setPort(this.port);
        connectionFactory.setHost(this.host);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);
        return connectionFactory;

    }

    @Bean
    public AmqpAdmin amqpAdmin(){
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setDefaultReceiveQueue(messageQueueName);
        template.setMessageConverter(messageConverter());
        return template;
    }



}
