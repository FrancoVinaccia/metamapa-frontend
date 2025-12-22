package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CriterioPertenenciaDTO {
    private String categoria;
    private RangoFechaDTO fecha;
    private LugarSinCoordsDTO lugar;
}
