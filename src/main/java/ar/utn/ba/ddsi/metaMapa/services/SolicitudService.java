package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.SolicitudEliminacionDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.SolicitudCambioInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.SolicitudEliminacionInputDTO;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService {

    private final MetaMapaApiService metaMapaApiService;

    public SolicitudService(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
    }

    public SolicitudEliminacionDTO crearSolicitudEliminacion(SolicitudEliminacionInputDTO solicitud) {
        return metaMapaApiService.crearSolicitudEliminacion(solicitud);
    }

    public SolicitudCambioInputDTO crearSolicitudCambio(Long idHecho, SolicitudCambioInputDTO solicitud) {
        return metaMapaApiService.crearSolicitudCambio(idHecho, solicitud);
    }
}
