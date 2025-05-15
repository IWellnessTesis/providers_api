package com.iwellness.providers.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeoServiceBusinessObject {
    private String serviceId;
    private String serviceName;
    private String coordenadaX;
    private String coordenadaY;
    private boolean estado;
    @JsonProperty("nombre_empresa")
    private String nombreEmpresa;
    private Long idProveedor;
    

}
