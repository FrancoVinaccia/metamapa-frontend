package ar.utn.ba.ddsi.metaMapa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SolicitudesEliminacionDTO {

    private Long id;
    private String justificacion;
    private EstadoSolicitud estado;
    private HechoAgregacionOutputDTO hecho;

}
