package com.iwellness.providers.Clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.iwellness.providers.DTO.ProveedorDTO;

@FeignClient(name = "proveedor-ms", url = "http://localhost:8082/usuarios")
public interface ProveedorFeignClient {

    @GetMapping("/buscar/{idUsuario}")
    ProveedorDTO  obtenerProveedor(@PathVariable("idUsuario") Long idUsuario);
}
