package com.iwellness.providers.DTO;

import lombok.Data;

@Data
public class GeoServiceBusinessObject {
    private String serviceId;
    private String serviceName;
    private String coordenadaX;
    private String coordenadaY;
    private boolean estado;

}
