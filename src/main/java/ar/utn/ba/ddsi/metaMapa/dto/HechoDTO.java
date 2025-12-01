package ar.utn.ba.ddsi.metaMapa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HechoDTO {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private LugarDTO lugar;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaAcontecimiento;
    private Date fechaCarga;
    private List<String> etiquetas;
    private List<MultimediaDTO> multimedia;
    private String cargaOrigen;
    private Long idUsuario;
    private Boolean anonimo;
    private  Boolean busquedaCurada;

    public String getImagen() {
        if (multimedia != null && !multimedia.isEmpty()) {
            return multimedia.get(0).getUrl();
        }
        return null;
    }
}
