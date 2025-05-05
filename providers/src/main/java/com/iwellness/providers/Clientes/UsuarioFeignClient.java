package com.iwellness.providers.Clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.iwellness.providers.DTO.UsuarioDTO;

@FeignClient(name = "usuario-ms", url = "http://localhost:8083/usuarios")
public interface UsuarioFeignClient {
    @GetMapping("/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable("id") String id);
}

