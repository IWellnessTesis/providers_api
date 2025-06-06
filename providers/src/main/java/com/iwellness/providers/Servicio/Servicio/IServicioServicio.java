package com.iwellness.providers.Servicio.Servicio;

import java.util.List;
import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.DTO.ServicioFiltroDTO;

public interface IServicioServicio {

    List<Servicio> BuscarTodos();

    Servicio BuscarPorId(Long id);

    Servicio Guardar(Servicio servicio);

    Servicio Actualizar(Servicio servicio);
    
    void Eliminar(Long id);

    List<Servicio> obtenerServiciosPorProveedor(Long idProveedor);

    void eliminarServiciosPorProveedor(Long idProveedor);

    List<Servicio> buscarServicios(ServicioFiltroDTO filtros);
}
