package ar.utn.ba.ddsi.metaMapa.models.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Contacto {
    private Long id;
    private TipoContacto tipoContacto;
    private String valor;
}
