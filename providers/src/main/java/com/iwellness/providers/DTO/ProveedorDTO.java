package com.iwellness.providers.DTO;

import lombok.Data;

@Data
public class ProveedorDTO {
    private Long _idUsuario;
    private String nombreEmpresa;
    private ProveedorInfo proveedorInfo;


    @Data
    public class ProveedorInfo {
        private String coordenadaX;
        private String coordenadaY;
    }


}
