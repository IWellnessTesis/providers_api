package com.iwellness.providers.DTO;

import lombok.Data;

@Data
public class ServicioFiltroDTO {
    private String nombre;
    private String descripcion;
    private Double precioMin;
    private Double precioMax;
    private String horario;
    private boolean estado;
    private String genero;
} 