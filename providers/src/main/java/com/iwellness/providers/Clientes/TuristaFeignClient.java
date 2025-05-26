package com.iwellness.providers.Clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.iwellness.providers.DTO.TuristaDTO;

@FeignClient(name = "turista-ms", url = "http://localhost:8082/usuarios")
public interface TuristaFeignClient {

    @GetMapping("/buscar/{idUsuario}")
    TuristaDTO  obtenerTurista(@PathVariable("idUsuario") Long idUsuario);
}