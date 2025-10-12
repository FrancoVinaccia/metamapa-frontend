package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class HechoDTO {
    //TODO: completar

    private Integer id;
    private String titulo;
    private String descripcion;
    private String imagen;

    public HechoDTO() {

    }
}
