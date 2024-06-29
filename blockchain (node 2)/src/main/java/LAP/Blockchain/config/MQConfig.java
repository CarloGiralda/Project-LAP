package LAP.Blockchain.config;



import LAP.Blockchain.service.ConsumerService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.ExceptionListener;
import java.util.UUID;

@Configuration
@EnableRabbit
public class MQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


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
        template.setMessageConverter(messageConverter());
        return template;
    }


    @Bean
    public MessageListenerAdapter listenerAdapter(ConsumerService consumerService, MessageConverter messageConverter) {
        return new MessageListenerAdapter(consumerService, "handleMessage");
        //adapter.setMessageConverter(messageConverter);
        //return adapter;
    }

    @Bean
    public String instanceId() {
        return "id-" + UUID.randomUUID();
    }

    @Bean
    public FanoutExchange broadcastExchange() {
        return new FanoutExchange("my-exchange");
    }

    @Bean
    public FanoutExchange broadcastExchange2() {
        return new FanoutExchange("my-exchange2");
    }

    @Bean
    public Queue instanceQueue(String instanceId) {
        return new Queue("my-queue-" + instanceId, false);
    }

    @Bean
    public Binding instanceBinding(Queue instanceQueue, FanoutExchange broadcastExchange) {
        return BindingBuilder.bind(instanceQueue).to(broadcastExchange);
    }

    @Bean
    public SimpleMessageListenerContainer container(String instanceId, ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("my-queue-" + instanceId);
        container.setMessageListener(listenerAdapter);
        return container;
    }
}
