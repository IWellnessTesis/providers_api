package com.iwellness.providers.Servicio;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.iwellness.providers.Clientes.PreferenciaFeignClient;
import com.iwellness.providers.Clientes.ProveedorFeignClient;
import com.iwellness.providers.DTO.ProveedorDTO;
import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Repositorio.IServicioRepositorio;

import jakarta.transaction.Transactional;

@Service
public class IServicioServicioImpl implements IServicioServicio {

    @Autowired
    private IServicioRepositorio servicioRepositorio;

    @Autowired
    private ProveedorFeignClient proveedorFeignClient;

    @Autowired
    private PreferenciaFeignClient preferenciaFeignClient;

    @Override
     //retorna una lista de servicios
    public List<Servicio> BuscarTodos() {
        return (List<Servicio>) servicioRepositorio.findAll();
    }

    @Override
    //retorna un servicio por id, si no lo encuentra, lanza una excepción NoSuchElementException
    public Servicio BuscarPorId(Long id) {
        return servicioRepositorio.findById(id).orElseThrow(() -> 
            new NoSuchElementException("Servicio no encontrado con ID: " + id));
    }

    @Override
    //guarda un servicio
    public Servicio Guardar(Servicio servicio) {
        // Verificar que exista un provedor con ese id
        ProveedorDTO proveedorDTO;
        try {
            proveedorDTO = proveedorFeignClient.obtenerProveedor(servicio.get_idProveedor());
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el microservicio de usuarios: " + e.getMessage());
        }

        if (proveedorDTO == null) {
            throw new IllegalArgumentException("El proveedor con ID " + servicio.get_idProveedor() + " no existe.");
        }
        return servicioRepositorio.save(servicio);
    }

    @Override
    //actualiza un servicio
    public Servicio Actualizar(Servicio servicio) {
        return servicioRepositorio.save(servicio);
    }

    @Override
    //elimina un servicio por id
    public void Eliminar(Long id) {
        servicioRepositorio.deleteById(id);
        preferenciaFeignClient.elimintarPreferenciasPorServicio(id);
    }

    public List<Servicio> obtenerServiciosPorProveedor(Long idProveedor) {
        // Verificar si el proveedor existe
        if (!servicioRepositorio.existsById(idProveedor)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
        }

        // Buscar los servicios del proveedor
        return servicioRepositorio.findBy_idProveedor(idProveedor);
    }

    @Transactional
    public void eliminarServiciosPorProveedor(Long idProveedor) {
        Optional<Servicio> servicioOpt = servicioRepositorio.findById(idProveedor);
        Servicio servicio = servicioOpt.orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        //Elimina las preferencias de ese servicio del microservicio de preferencias
        preferenciaFeignClient.elimintarPreferenciasPorServicio(servicio.get_idServicio());
        
        servicioRepositorio.deleteBy_idProveedor(idProveedor);
    }
}