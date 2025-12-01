package ar.utn.ba.ddsi.metaMapa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudesCambioDTO {

    private Long id;
    private Long idHecho;
    private String titulo;                // nuevo_titulo o título recibido
    private String descripcion;           // nueva_descripcion
    private String categoria;
    private LugarDTO lugar;               // puede venir sólo con id
    private Date fechaAcontecimiento;     // nueva_fecha_hecho
    private List<MultimediaDTO> multimedia;
    private Boolean resuelta;

    // --- Helpers que la vista espera ---

    // Hecho: la vista usa 'hechoTitulo'. Si backend entrega sólo 'titulo' del cambio, lo reutilizamos.
    public String getHechoTitulo() {
        return titulo;
    }

    // 'tiempo' suele calcularse en cliente; devolvemos null para no romper la vista
    public String getTiempo() {
        return null;
    }

    // Usuario: si no hay info, devolvemos null (la plantilla muestra según hecho.anonimo / idUsuario cuando exista)
    public String getUsuario() {
        return null;
    }

    // Ubicación amigable a partir de 'lugar' si contiene subcampos
    public String getUbicacion() {
        if (lugar == null) return null;
        StringBuilder sb = new StringBuilder();
        if (lugar.getLocalidad() != null && !lugar.getLocalidad().isBlank()) {
            sb.append(lugar.getLocalidad());
        } else if (lugar.getCiudad() != null && !lugar.getCiudad().isBlank()) {
            sb.append(lugar.getCiudad());
        } else if (lugar.getProvincia() != null && !lugar.getProvincia().isBlank()) {
            sb.append(lugar.getProvincia());
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    // Información adicional que la vista muestra (si hubiera multimedia o campos extra)
    public String getInformacionAdicional() {
        // Si hay multimedia, devolver un resumen; por ahora devolvemos null
        return null;
    }

    // Estado textual que la vista espera (APROBADA / PENDIENTE)
    public String getEstado() {
        if (resuelta == null) return null;
        return Boolean.TRUE.equals(resuelta) ? "APROBADA" : "PENDIENTE";
    }
}
