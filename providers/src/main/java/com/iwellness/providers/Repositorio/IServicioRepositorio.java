package com.iwellness.providers.Repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iwellness.providers.Entidad.Servicio;

public interface IServicioRepositorio extends CrudRepository<Servicio, Long> {
    
}
