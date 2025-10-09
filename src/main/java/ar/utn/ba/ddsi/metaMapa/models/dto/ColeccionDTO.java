package ar.utn.ba.ddsi.metaMapa.models.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class ColeccionDTO {
    //TODO: completar
    private Long id;
    private String titulo;
    private String descripcion;

    public ColeccionDTO() {

    }
}
