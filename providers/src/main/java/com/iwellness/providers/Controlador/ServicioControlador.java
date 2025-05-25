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
import org.springframework.web.bind.annotation.RestController;

import com.iwellness.providers.Clientes.ProveedorFeignClient;
import com.iwellness.providers.DTO.GeoServiceBusinessObject;
import com.iwellness.providers.DTO.ProveedorDTO;
import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Servicio.Servicio.IServicioServicio;

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
        System.out.println("Proveedor recibido: " + proveedor);
        System.out.println("ProveedorInfo: " + proveedor.getProveedorInfo());
        System.out.println("idUsuario: " + proveedor.getId());
        System.out.println("idProveedor: " + proveedor.getProveedorInfo().getId());
        Long idUsuario = proveedor.getId();
        Long idProveedor = proveedor.getProveedorInfo().getId();

        // Acceder a las coordenadas anidadas
        String coordenadaX = proveedor.getProveedorInfo().getCoordenadaX();
        String coordenadaY = proveedor.getProveedorInfo().getCoordenadaY();

        // Crear objeto para análisis
        GeoServiceBusinessObject geoObj = new GeoServiceBusinessObject();

        geoObj.setServiceId(servicioGuardado.get_idServicio().toString());
        geoObj.setServiceName(servicioGuardado.getNombre());
        geoObj.setCoordenadaX(coordenadaX);
        geoObj.setCoordenadaY(coordenadaY);
        geoObj.setIdProveedor(idProveedor);
        geoObj.setIdUsuario(idUsuario);
        geoObj.setNombreEmpresa(proveedor.getProveedorInfo().getNombreEmpresa());
        geoObj.setEstado(servicioGuardado.isEstado());


        // Enviar por RabbitMQ
        rabbitTemplate.convertAndSend("message_exchange_services", "my_routing_key_service", geoObj);

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
            System.out.println("Proveedor recibido: " + proveedor);
            System.out.println("ProveedorInfo: " + proveedor.getProveedorInfo());
            System.out.println("idUsuario: " + proveedor.getId());
            System.out.println("idProveedor: " + proveedor.getProveedorInfo().getId());
            Long idUsuario = proveedor.getId();
            Long idProveedor = proveedor.getProveedorInfo().getId();

            if (proveedor.getProveedorInfo() == null) {
                throw new RuntimeException("El proveedor no tiene información de coordenadas (campo 'proveedorInfo' es null)");
            }

            if (proveedor.getProveedorInfo().getNombreEmpresa() == null || proveedor.getProveedorInfo().getNombreEmpresa().trim().isEmpty()) {
                throw new RuntimeException("El proveedor no tiene un nombre de empresa válido");
            }

            // Acceder a las coordenadas anidadas
            String coordenadaX = proveedor.getProveedorInfo().getCoordenadaX();
            String coordenadaY = proveedor.getProveedorInfo().getCoordenadaY();

            // Crear objeto para análisis
            GeoServiceBusinessObject geoObj = new GeoServiceBusinessObject();
            geoObj.setServiceId(servicioGuardado.get_idServicio().toString());
            geoObj.setServiceName(servicioGuardado.getNombre());
            geoObj.setCoordenadaX(coordenadaX);
            geoObj.setCoordenadaY(coordenadaY);
            geoObj.setNombreEmpresa(proveedor.getProveedorInfo().getNombreEmpresa());
            geoObj.setIdProveedor(idProveedor);
            geoObj.setIdUsuario(idUsuario);
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

}
