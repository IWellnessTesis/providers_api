package com.iwellness.providers.Servicio.Reserva;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwellness.providers.Entidad.Reserva;
import com.iwellness.providers.Repositorio.IReservaRepositorio;

@Service
public class IReservaServicioImpl implements IReservaServicio{

    @Autowired
    private IReservaRepositorio reservaRepositorio;

    @Override
     //retorna una lista de reservas
    public List<Reserva> BuscarTodos() {
        return (List<Reserva>) reservaRepositorio.findAll();
    }

    @Override
    //retorna un reserva por id, si no lo encuentra, lanza una excepción NoSuchElementException
    public Reserva BuscarPorId(Long id) {
        return reservaRepositorio.findById(id).orElseThrow(() -> 
            new NoSuchElementException("Reserva no encontrado con ID: " + id));
    }

    @Override
    //guarda un reserva
    public Reserva Guardar(Reserva reserva) {
        return reservaRepositorio.save(reserva);
    }

    @Override
    //actualiza un reserva
    public Reserva Actualizar(Reserva reserva) {
        return reservaRepositorio.save(reserva);
    }

    @Override
    //elimina un reserva por id
    public void Eliminar(Long id) {
        reservaRepositorio.deleteById(id);
    }

    @Override
    public List<Reserva> obtenerReservasPorTurista(Long idTurista){
        return reservaRepositorio.findBy_idTurista(idTurista);
    }

    
}
