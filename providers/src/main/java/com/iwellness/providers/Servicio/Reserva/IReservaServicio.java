package com.iwellness.providers.Servicio.Reserva;

import java.util.List;

import com.iwellness.providers.Entidad.Reserva;

public interface IReservaServicio {

    List<Reserva> BuscarTodos();

    Reserva BuscarPorId(Long id);

    Reserva Guardar(Reserva reserva);

    Reserva Actualizar(Reserva reserva);
    
    void Eliminar(Long id);
    
    List<Reserva> obtenerReservasPorTurista(Long idTurista);
}
