package com.iwellness.providers.Servicio.Rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class MensajeServiceProviders {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MensajeServiceProviders(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensaje(String mensaje) {
        String exchange = "my_exchange";       // Nombre del exchange
        String routingKey = "my_routing_key";  // Clave de enrutamiento

        rabbitTemplate.convertAndSend(exchange, routingKey, mensaje);
        System.out.println("Mensaje enviado: " + mensaje);
    }
}
