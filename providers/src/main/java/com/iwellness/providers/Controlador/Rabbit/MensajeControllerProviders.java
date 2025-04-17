package com.iwellness.providers.Controlador.Rabbit;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Servicio.Rabbit.MensajeServiceProvidersConfig;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/servicio/publish")
@RequiredArgsConstructor
public class MensajeControllerProviders {


 
    private final RabbitTemplate template;

    @PostMapping("/mensaje/enviar")
    public String enviarMensaje(@RequestBody Servicio servicio) {
        try {
            this.template.convertAndSend(MensajeServiceProvidersConfig.EXCHANGE_NAME, 
            MensajeServiceProvidersConfig.ROUTING_KEY_PROVIDER, 
            servicio);

            return "Servicio publicado: " + servicio.getNombre() + " con id: " + servicio.get_idServicio();
        } catch (Exception e) {
            return "Error al serializar el servicio: " + e.getMessage();
        }
    }
}
