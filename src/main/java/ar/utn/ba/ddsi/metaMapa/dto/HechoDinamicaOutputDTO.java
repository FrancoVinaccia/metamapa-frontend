package ar.utn.ba.ddsi.metaMapa.dto;

import ar.utn.ba.ddsi.metaMapa.dto.LugarDTO;
import ar.utn.ba.ddsi.metaMapa.dto.MultimediaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HechoDinamicaOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private LugarDTO lugar;
    private Date fechaAcontecimiento;
    private Date fechaCarga;
    private List<String> etiquetas;
    private List<MultimediaDTO> multimedia;

}
