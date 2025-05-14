package com.iwellness.providers.Repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iwellness.providers.Entidad.Reserva;

public interface IReservaRepositorio extends CrudRepository<Reserva, Long>{

    List<Reserva> findBy_idTurista(Long idTurista);
    List<Reserva> findBy_idServicio(Long idServicio);
} 
