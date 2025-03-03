package com.iwellness.providers.Repositorio;

import org.springframework.data.repository.CrudRepository;

import com.iwellness.providers.Entidad.Servicio;

public interface IServicioRepositorio extends CrudRepository<Servicio, Long> {
    
}
