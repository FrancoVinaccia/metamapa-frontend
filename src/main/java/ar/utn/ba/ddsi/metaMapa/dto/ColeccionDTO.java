// java
// File: src/main/java/ar/utn/ba/ddsi/metaMapa/dto/ColeccionDTO.java
package ar.utn.ba.ddsi.metaMapa.dto;

import ar.utn.ba.ddsi.metaMapa.dto.input.CriterioPertenenciaInputDTO;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionDTO {
    private String idColeccion;
    private String titulo;
    private String descripcion;
    private List<String> fuentes;
    private CriterioPertenenciaInputDTO criterios;
    private String metodoConsenso;
    private PageHechoDTO hechos;
    private String imagen;


    public List<HechoDTO> getHechosLista() {
        if (this.hechos == null || this.hechos.getElementos() == null) {
            return List.of();
        }
        return this.hechos.getElementos();
    }
}
