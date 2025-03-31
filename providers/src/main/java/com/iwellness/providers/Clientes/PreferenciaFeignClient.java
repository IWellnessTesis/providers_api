package com.iwellness.providers.Clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "preferencias-ms", url = "http://localhost:8081/api/servicioXPreferencia")
public interface PreferenciaFeignClient {

    @DeleteMapping("/eliminarPorServicio/{idServicio}")
    void elimintarPreferenciasPorServicio(@PathVariable("idServicio") Long idServicio);

}
