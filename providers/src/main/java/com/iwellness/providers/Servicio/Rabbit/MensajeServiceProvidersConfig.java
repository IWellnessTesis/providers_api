package com.iwellness.providers.Servicio.Rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MensajeServiceProvidersConfig {
    public static final String EXCHANGE_NAME = "message_exchange_services";
    public static final String QUEUE_NAME = "my_queue_busqueda_servicio"; // caso 3
    public static final String QUEUE_NAME_ESTADO_CIVIL = "my_queue_estado_civil"; // caso 4
    public static final String ROUTING_KEY_PROVIDER = "my_routing_key_busqueda_servicio";
    public static final String ROUTING_KEY_ESTADO_CIVIL = "my_routing_key_estado_civil"; 
    public static final String QUEUE_NAME_SERVICE = "queue_services"; // caso 1
    public static final String ROUTING_KEY_SERVICE = "my_routing_key_service";

    @Bean
    public TopicExchange topicexchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connection) {
        final var template = new RabbitTemplate(connection);
        template.setMessageConverter(messageConverter());
        return template;
    }
    @Bean
    public Queue queueBusquedaServicio() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue queueEstadoCivil() {
        return new Queue(QUEUE_NAME_ESTADO_CIVIL, true);
    }

    @Bean
    public Binding bindingBusquedaServicio(Queue queueBusquedaServicio, TopicExchange exchange) {
        return BindingBuilder.bind(queueBusquedaServicio).to(exchange).with(ROUTING_KEY_PROVIDER);
    }

    @Bean
    public Binding bindingEstadoCivil(Queue queueEstadoCivil, TopicExchange exchange) {
        return BindingBuilder.bind(queueEstadoCivil).to(exchange).with(ROUTING_KEY_ESTADO_CIVIL);
    }


    
}
