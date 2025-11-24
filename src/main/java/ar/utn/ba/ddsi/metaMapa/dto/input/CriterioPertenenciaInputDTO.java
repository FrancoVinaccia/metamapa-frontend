package ar.utn.ba.ddsi.metaMapa.dto.input;


import ar.utn.ba.ddsi.metaMapa.dto.LugarDTO;
import ar.utn.ba.ddsi.metaMapa.dto.RangoFechaDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CriterioPertenenciaInputDTO {
    private String categoria;
    private RangoFechaDTO fecha;
    private LugarDTO lugar;
}
