package com.iwellness.providers.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProveedorDTO {
    private Long _idUsuario;
    private ProveedorInfo proveedorInfo;


    @Data
    public class ProveedorInfo {
        private String coordenadaX;
        private String coordenadaY;
        @JsonProperty("nombre_empresa")
        private String nombreEmpresa;
    }


}
