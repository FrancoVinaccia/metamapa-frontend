package ar.utn.ba.ddsi.metaMapa.dto.input;

import ar.utn.ba.ddsi.metaMapa.dto.CriterioPertenenciaDTO;
import ar.utn.ba.ddsi.metaMapa.dto.LugarDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ColeccionInputDTO {
    private String titulo;

    private String descripcion;

    private CriterioPertenenciaDTO criterios;

    private List<String> fuentes;

    private String metodoConsenso;
}