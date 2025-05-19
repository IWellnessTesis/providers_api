package com.iwellness.providers.Controlador;

import java.util.Optional;

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

import com.iwellness.providers.Clientes.TuristaFeignClient;
import com.iwellness.providers.DTO.ReservaAnalisisDTO;
import com.iwellness.providers.DTO.TuristaDTO;
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
    private TuristaFeignClient turistaClient;


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
public ResponseEntity<?> guardar(@RequestBody Reserva reserva) {
    try {

        TuristaDTO turistaDTO = turistaClient.obtenerTurista(reserva.get_idTurista());
        System.out.println("Reserva a guardar: " + reserva);
        System.out.println("idServicio: " + reserva.get_idServicio());
        System.out.println("idUsuario: " + turistaDTO.getId());
        Long idTurista = turistaDTO.getId();

        Reserva reservaGuardada = reservaServicio.Guardar(reserva);

        ReservaAnalisisDTO reservaDTO = new ReservaAnalisisDTO();
        reservaDTO.setId(reservaGuardada.getId());
        reservaDTO.set_idServicio(reservaGuardada.get_idServicio());
        reservaDTO.setFechaServicio(reservaGuardada.getFechaServicio());
        reservaDTO.setFechaReserva(reservaGuardada.getFechaReserva());
        reservaDTO.setEstado(reservaGuardada.getEstado());
        reservaDTO.set_idTurista(idTurista);

        //Enviar a RabbitMQ
        reservaDTO.set_idTurista(idTurista);
        mensajeService.enviarMensajeReserva(reservaDTO);

        return ResponseEntity.ok(reservaGuardada);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar la reserva: " + e.getMessage());
    }
}

   @PutMapping("/update")
public ResponseEntity<?> Actualizar(@RequestBody ReservaAnalisisDTO reservaDTO) {
    System.out.println("Reserva a actualizar: " + reservaDTO);
    try {
        Optional<Reserva> reservaExistenteOpt = Optional.ofNullable(reservaServicio.BuscarPorId(reservaDTO.getId()));

        if (reservaExistenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada con id: " + reservaDTO.getId());
        }

        Reserva reservaExistente = reservaExistenteOpt.get();

        TuristaDTO turistaDTO = turistaClient.obtenerTurista(reservaDTO.get_idTurista());
        Long idTurista = turistaDTO.getId();

        // Actualizar los campos
        reservaExistente.setId(reservaDTO.getId());
        reservaExistente.set_idServicio(reservaDTO.get_idServicio());
        reservaExistente.setFechaServicio(reservaDTO.getFechaServicio());
        reservaExistente.setFechaReserva(reservaDTO.getFechaReserva());
        reservaExistente.setEstado(reservaDTO.getEstado());
        reservaExistente.set_idTurista(idTurista);

        Reserva reservaActualizada = reservaServicio.Actualizar(reservaExistente);

        // Enviar a RabbitMQ
        reservaDTO.set_idTurista(idTurista);
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
