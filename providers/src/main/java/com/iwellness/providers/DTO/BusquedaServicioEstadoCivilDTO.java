package com.iwellness.providers.DTO;

import lombok.Data;

@Data
public class BusquedaServicioEstadoCivilDTO {
    private String userId;
    private String userStatus;  // "Soltero", "Casado", etc.
    private String userGenre;   // "Masculino", "Femenino", etc.
    private String serviceName;
    private String searchDate;
}
