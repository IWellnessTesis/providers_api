package com.iwellness.providers.Servicio.Rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.iwellness.providers.DTO.ReservaAnalisisDTO;

@Service
public class MensajeServiceProviders {


    
    private final RabbitTemplate rabbitTemplate;

    public MensajeServiceProviders(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensaje(String mensaje) {
        rabbitTemplate.convertAndSend(MensajeServiceProvidersConfig.EXCHANGE_NAME, MensajeServiceProvidersConfig.ROUTING_KEY_PROVIDER, mensaje);
        System.out.println("Mensaje enviado: " + mensaje);
    }

    public void enviarMensajeReserva(ReservaAnalisisDTO reservaAnalisis) {
        rabbitTemplate.convertAndSend(MensajeServiceProvidersConfig.EXCHANGE_NAME, MensajeServiceProvidersConfig.ROUTING_KEY_RESERVA, reservaAnalisis);
        System.out.println("Mensaje de análisis de reserva enviado: " + reservaAnalisis);
    }
}
