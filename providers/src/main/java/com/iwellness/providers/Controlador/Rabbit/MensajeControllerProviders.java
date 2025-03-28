package com.iwellness.providers.Controlador.Rabbit;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.iwellness.providers.Servicio.Rabbit.MensajeServiceProviders;

public class MensajeControllerProviders {
          private final MensajeServiceProviders mensajeServiceProviders;

    public MensajeControllerProviders(MensajeServiceProviders mensajeServiceProviders) {
        this.mensajeServiceProviders = mensajeServiceProviders;
    }

    @PostMapping("/enviar")
    public String enviarMensaje(@RequestBody String mensaje) {
        mensajeServiceProviders.enviarMensaje(mensaje);
        return "Mensaje enviado: " + mensaje;
    }
}
