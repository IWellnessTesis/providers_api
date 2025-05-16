package com.iwellness.providers.DTO;

import lombok.Data;

@Data
public class TuristaDTO {
    private Long id; //Usuario
    private TuristaInfo turistaInfo;


    @Data
    public static class TuristaInfo {
        private Long id;
    }
    
}
