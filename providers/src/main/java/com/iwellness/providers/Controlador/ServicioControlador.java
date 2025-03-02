package com.iwellness.providers.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iwellness.providers.Entidad.Servicio;
import com.iwellness.providers.Servicio.IServicioServicio;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/servicio")
public class ServicioControlador {
    
    @Autowired
    private IServicioServicio servicioServicio;
    
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

    @PostMapping("/save/{servicio}")
    public ResponseEntity<?> Guardar(@PathVariable Servicio servicio){
try {
            return ResponseEntity.ok(servicioServicio.Guardar(servicio));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar el servicio: " + e.getMessage());
        }    }

    @GetMapping("/update/{servicio}")
    public ResponseEntity<?> Actualizar(@PathVariable Servicio servicio){
        try {
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
    
}
