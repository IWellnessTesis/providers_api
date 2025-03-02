package com.iwellness.providers.Servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Repositorio.IServicioRepositorio;

@Service
public class IServicioServicioImpl implements IServicioServicio {

    @Autowired
    private IServicioRepositorio servicioRepositorio;

    @Override
    public List<Servicio> BuscarTodos() {
        return (List<Servicio>) servicioRepositorio.findAll();
    }

    @Override
    public Servicio BuscarPorId(Long id) {
        return servicioRepositorio.findById(id).get();
    }

    @Override
    public Servicio Guardar(Servicio servicio) {
        return servicioRepositorio.save(servicio);
    }

    @Override
    public Servicio Actualizar(Servicio servicio) {
        return servicioRepositorio.save(servicio);
    }

    @Override
    public void Eliminar(Long id) {
        servicioRepositorio.deleteById(id);
    }
}