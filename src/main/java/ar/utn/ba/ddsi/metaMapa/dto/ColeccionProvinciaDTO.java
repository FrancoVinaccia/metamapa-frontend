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
public class ColeccionProvinciaDTO {
    private String idColeccion;
    private String provincia;
    private Integer cantidad;

    // ✅ Para que tu HTML actual siga funcionando con row.coleccion
    public String getColeccion() {
        return idColeccion;
    }
}
