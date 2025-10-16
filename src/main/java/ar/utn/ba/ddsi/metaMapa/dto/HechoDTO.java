package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HechoDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private String imagen;

    // Nuevos campos para la vista
    private String categoria;
    private String ubicacion;
    private LocalDate fecha;
}
