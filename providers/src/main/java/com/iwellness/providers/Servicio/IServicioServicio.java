package com.iwellness.providers.Servicio;

import java.util.List;
import com.iwellness.providers.Entidad.Servicio;

public interface IServicioServicio {

    List<Servicio> BuscarTodos();

    Servicio BuscarPorId(Long id);

    Servicio Guardar(Servicio servicio);

    Servicio Actualizar(Servicio servicio);
    
    void Eliminar(Long id);
    List<Servicio> obtenerServiciosPorProveedor(Long idProveedor);
}
