package com.iwellness.providers.Controlador;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwellness.providers.Clientes.ProveedorFeignClient;
import com.iwellness.providers.DTO.BusquedaServicioDTO;
import com.iwellness.providers.DTO.BusquedaServicioEstadoCivilDTO;
import com.iwellness.providers.DTO.GeoServiceBusinessObject;
import com.iwellness.providers.DTO.ProveedorDTO;
import com.iwellness.providers.DTO.ServicioFiltroDTO;
import com.iwellness.providers.DTO.UsuarioDTO;
import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Servicio.IServicioServicio;

@RestController
@RequestMapping("/api/servicio")
@CrossOrigin(origins = "*")
public class ServicioControlador {
    
    @Autowired
    private IServicioServicio servicioServicio;

    @Autowired
    private ProveedorFeignClient proveedorClient;


    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @GetMapping("/all")
    public ResponseEntity<?> BuscarTodos(){
        return ResponseEntity.ok(servicioServicio.BuscarTodos());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> BuscarPorId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(servicioServicio.BuscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el servicio con ID: " + id);
        }
    }

    @PostMapping("/save")
public ResponseEntity<?> Guardar(@RequestBody Servicio servicio){
    try {
        Servicio servicioGuardado = servicioServicio.Guardar(servicio);

        // Obtener datos del proveedor
        ProveedorDTO proveedor = proveedorClient.obtenerProveedor(servicio.get_idProveedor());

        if (proveedor.getProveedorInfo() == null) {
            throw new RuntimeException("El proveedor no tiene información de coordenadas (campo 'proveedorInfo' es null)");
        }

        // Acceder a las coordenadas anidadas
        String coordenadaX = proveedor.getProveedorInfo().getCoordenadaX();
        String coordenadaY = proveedor.getProveedorInfo().getCoordenadaY();

        System.out.println("Proveedor recibido:");
        System.out.println("Coordenada X: " + coordenadaX);
        System.out.println("Coordenada Y: " + coordenadaY);

        // Crear objeto para análisis
        GeoServiceBusinessObject geoObj = new GeoServiceBusinessObject();
        geoObj.setServiceId(servicioGuardado.get_idServicio().toString());
        geoObj.setServiceName(servicioGuardado.getNombre());
        geoObj.setCoordenadaX(coordenadaX);
        geoObj.setCoordenadaY(coordenadaY);
        geoObj.setEstado(servicioGuardado.isEstado());

        System.out.println("Enviando a RabbitMQ: " + geoObj);
        // Enviar por RabbitMQ
        rabbitTemplate.convertAndSend("message_exchange_services", "my_routing_key_service", geoObj);
        System.out.println("Mensaje enviado a RabbitMQ");
        return ResponseEntity.ok(servicioGuardado);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar el servicio: " + e.getMessage());
    }    
}

    @PutMapping("/update/{servicio}")
    public ResponseEntity<?> Actualizar(@RequestBody Servicio servicio){
        try {

            Servicio servicioGuardado = servicioServicio.Actualizar(servicio);
            // Obtener datos del proveedor
            ProveedorDTO proveedor = proveedorClient.obtenerProveedor(servicio.get_idProveedor());

            // Acceder a las coordenadas anidadas
            String coordenadaX = proveedor.getProveedorInfo().getCoordenadaX();
            String coordenadaY = proveedor.getProveedorInfo().getCoordenadaY();

            // Crear objeto para análisis
            GeoServiceBusinessObject geoObj = new GeoServiceBusinessObject();
            geoObj.setServiceId(servicioGuardado.get_idServicio().toString());
            geoObj.setServiceName(servicioGuardado.getNombre());
            geoObj.setCoordenadaX(coordenadaX);
            geoObj.setCoordenadaY(coordenadaY);
            geoObj.setEstado(servicioGuardado.isEstado());

            // Enviar por RabbitMQ
            rabbitTemplate.convertAndSend("message_exchange_services", "my_routing_key_service", geoObj);


            return ResponseEntity.ok(servicioServicio.Actualizar(servicio));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el servicio: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> Eliminar(@PathVariable Long id){
        try {
            servicioServicio.Eliminar(id);
            return ResponseEntity.ok("Entidad eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la entidad con ID: " + id);
        }
    }

    @GetMapping("/{idProveedor}/servicios")
    public ResponseEntity<List<Servicio>> obtenerServicios(@PathVariable Long idProveedor) {
        List<Servicio> servicios = servicioServicio.obtenerServiciosPorProveedor(idProveedor);
        return ResponseEntity.ok(servicios);
    }

    @DeleteMapping("/eliminarPorProveedor/{idProveedor}")
    public ResponseEntity<String> eliminarServiciosPorProveedor(@PathVariable Long idProveedor) {
        servicioServicio.eliminarServiciosPorProveedor(idProveedor);
        return ResponseEntity.ok("Servicios del proveedor eliminados correctamente");
    }
/* 
    //CASO: 3 y 4
    @PostMapping("/buscar_servicio")
    public ResponseEntity<?> buscarServicio(@RequestParam("user_id") String userId, @RequestBody ServicioFiltroDTO filtros) {
        try {
            List<Servicio> servicios = servicioServicio.buscarServicios(filtros);
            ProveedorDTO proveedor = proveedorClient.obtenerProveedor(Long.parseLong(userId));
            
            for (Servicio servicio : servicios) {
                // COLA -> Proveedores con mas servicios reciben más busquedas -> relacion proveedor-servicio
                BusquedaServicioDTO dto = new BusquedaServicioDTO();
                dto.setProviderId(servicio.get_idProveedor().toString());
                dto.setServiceId(servicio.get_idServicio().toString());
                dto.setUserId(userId);
                dto.setTimestamp(LocalDateTime.now().toString());

                //Informacion de usuario -> Estado civil -> Caso 4
                BusquedaServicioEstadoCivilDTO dtoEstadoCivil = new BusquedaServicioEstadoCivilDTO();
                dtoEstadoCivil.setUserId(userId);
                dtoEstadoCivil.setUserStatus(usuario.getEstadoCivil());
                dtoEstadoCivil.setUserGenre(filtros.getGenero());
                dtoEstadoCivil.setServiceName(servicio.getNombre());
                dtoEstadoCivil.setSearchDate(LocalDateTime.now().toString());
    
                rabbitTemplate.convertAndSend("message_exchange_services", "my_queue_busqueda_servicio", dto);
                rabbitTemplate.convertAndSend("message_exchange_services", "my_queue_estado_civil", dtoEstadoCivil);
            }
    
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar servicios: " + e.getMessage());
        }
    }
*/
}
