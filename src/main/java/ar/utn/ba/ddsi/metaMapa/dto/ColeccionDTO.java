// java
// File: src/main/java/ar/utn/ba/ddsi/metaMapa/dto/ColeccionDTO.java
package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private PageHechoDTO hechos;


    public List<HechoDTO> getHechosLista() {
        if (this.hechos == null || this.hechos.getElementos() == null) {
            return List.of();
        }
        return this.hechos.getElementos();
    }
}
