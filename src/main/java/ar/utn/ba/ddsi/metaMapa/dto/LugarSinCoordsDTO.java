package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LugarSinCoordsDTO {
    private long id;
    private String localidad;
    private String ciudad;
    private String provincia;
}
