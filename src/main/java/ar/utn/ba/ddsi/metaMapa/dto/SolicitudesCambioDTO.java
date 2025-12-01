package ar.utn.ba.ddsi.metaMapa.dto;

import java.sql.Date;
import java.util.List;


public class SolicitudesCambioDTO {

    private Long id;
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private LugarDTO lugar;
    private Date fechaAcontecimiento;
    private List<MultimediaDTO> multimedia;
    private Boolean resuelta;

}
