package com.iwellness.providers.DTO;

import lombok.Data;


@Data
public class ReservaAnalisisDTO {
    private Long _idServicio;
    private Long _idTurista;
    private Long _idUsuario;
    private String fechaServicio;
    private String fechaReserva;
    private String estado;
}