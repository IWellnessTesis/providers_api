package com.iwellness.providers.Controlador;

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

import com.iwellness.providers.DTO.ReservaAnalisisDTO;
import com.iwellness.providers.Entidad.Reserva;
import com.iwellness.providers.Servicio.Rabbit.MensajeServiceProviders;
import com.iwellness.providers.Servicio.Reserva.IReservaServicio;
import com.iwellness.providers.Servicio.Servicio.IServicioServicio;



@RestController
@RequestMapping("/api/reserva")
@CrossOrigin(origins = "*")
public class ReservaControlador {
    
    @Autowired
    private IReservaServicio reservaServicio;



    @Autowired
    private MensajeServiceProviders mensajeService;

     @GetMapping("/all")
    public ResponseEntity<?> BuscarTodos(){
        return ResponseEntity.ok(reservaServicio.BuscarTodos());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> BuscarPorId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(reservaServicio.BuscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la reserva con ID: " + id);
        }
    }

    @PostMapping("/save")
public ResponseEntity<?> guardar(@RequestBody ReservaAnalisisDTO reservaDTO) {
    try {
        
        Reserva reserva = new Reserva();
        reserva.set_idServicio(reservaDTO.get_idServicio());
        reserva.set_idTurista(reservaDTO.get_idTurista());
        reserva.setFechaServicio(reservaDTO.getFechaServicio());
        reserva.setFechaReserva(reservaDTO.getFechaReserva());
        reserva.setEstado(reservaDTO.getEstado());

        Reserva reservaGuardada = reservaServicio.Guardar(reserva);

        //Enviar a RabbitMQ
        mensajeService.enviarMensajeReserva(reservaDTO);

        return ResponseEntity.ok(reservaGuardada);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar la reserva: " + e.getMessage());
    }
}

    @PutMapping("/update")
    public ResponseEntity<?> Actualizar(@RequestBody ReservaAnalisisDTO reservaDTO){
        System.out.println("Reserva a actualizar: " + reservaDTO);
        try {
             // Mapear el DTO a la entidad Reserva
        Reserva reserva = new Reserva();
        reserva.set_idServicio(reservaDTO.get_idServicio());
        reserva.set_idTurista(reservaDTO.get_idTurista());
        reserva.setFechaServicio(reservaDTO.getFechaServicio());
        reserva.setFechaReserva(reservaDTO.getFechaReserva());
        reserva.setEstado(reservaDTO.getEstado());

        Reserva reservaActualizada = reservaServicio.Actualizar(reserva);

        // Enviar a RabbitMQ
        mensajeService.enviarMensajeReserva(reservaDTO);

        return ResponseEntity.ok(reservaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar la reserva: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> Eliminar(@PathVariable Long id){
        try {
            reservaServicio.Eliminar(id);
            return ResponseEntity.ok("Entidad eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la entidad con ID: " + id);
        }
    }

    @GetMapping("/reservasTurista/{id}")
    public ResponseEntity<?> reservasPorTurista(@PathVariable Long id) {
        return ResponseEntity.ok(reservaServicio.obtenerReservasPorTurista(id));
    }
    
}
