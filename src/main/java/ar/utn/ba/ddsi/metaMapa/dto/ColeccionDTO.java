package ar.utn.ba.ddsi.metaMapa.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ColeccionDTO {
    //TODO: completar
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private List<HechoDTO> hechos = new ArrayList<>();;

    public ColeccionDTO() {

    }
}
