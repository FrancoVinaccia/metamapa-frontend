package ar.utn.ba.ddsi.metaMapa.dto.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class SolicitudEliminacionInputDTO {
    private Long idHecho;
    private String justificacion;

}
