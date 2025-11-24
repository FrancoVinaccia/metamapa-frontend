package ar.utn.ba.ddsi.metaMapa.dto.input;

import ar.utn.ba.ddsi.metaMapa.dto.LugarDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class HechoInputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private LugarDTO lugar;
    private String fecha;
    private Long idUsuario;
    private Boolean publicadoAnonimo = false;
}
