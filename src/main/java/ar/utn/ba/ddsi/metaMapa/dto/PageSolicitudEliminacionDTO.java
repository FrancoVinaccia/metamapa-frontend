package ar.utn.ba.ddsi.metaMapa.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageSolicitudEliminacionDTO {
    private Integer totalPages;
    private Long totalElements;
    private Integer currentPage;
    private List<SolicitudesEliminacionDTO> elementos;
}
