package ar.utn.ba.ddsi.metaMapa.dto.input;

import ar.utn.ba.ddsi.metaMapa.dto.LugarDTO;
import ar.utn.ba.ddsi.metaMapa.dto.MultimediaDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SolicitudCambioInputDTO {
    private String titulo = null;
    private String descripcion = null;
    private String categoria = null;
    private LugarDTO lugar = null;
    private String fechaAcontecimiento = null;

    private List<MultimediaDTO> archivosMultimedia = new ArrayList<>();
}