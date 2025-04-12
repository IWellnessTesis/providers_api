package com.iwellness.providers.Repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iwellness.providers.Entidad.Servicio;

public interface IServicioRepositorio extends CrudRepository<Servicio, Long> {

    List<Servicio> findBy_idProveedor(Long idProveedor);

    void deleteBy_idProveedor(Long idProveedor);

    boolean existsBy_idProveedor(Long idProveedor);
    
}
