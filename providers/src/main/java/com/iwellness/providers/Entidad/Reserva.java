package com.iwellness.providers.Entidad;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "reserva")
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_turista")
    private Long _idTurista;

    @Column(name = "id_servicio")
    private Long _idServicio;

    @Column(name = "fecha_reserva")
    private String fechaReserva;   

    @Column(name = "fecha_servicio")
    private String fechaServicio;

    private String estado;
}
