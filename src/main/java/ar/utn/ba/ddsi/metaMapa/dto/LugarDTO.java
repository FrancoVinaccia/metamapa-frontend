package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LugarDTO {
    private Long id;
    private Double latitud;
    private Double longitud;
    private String localidad;
    private String ciudad;
    private String provincia;
}
