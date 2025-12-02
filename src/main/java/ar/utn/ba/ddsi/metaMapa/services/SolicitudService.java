package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.*;
import ar.utn.ba.ddsi.metaMapa.dto.input.SolicitudCambioInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.SolicitudEliminacionInputDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

    private final MetaMapaApiService metaMapaApiService;

    public SolicitudService(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
    }

    public SolicitudEliminacionDTO crearSolicitudEliminacion(SolicitudEliminacionInputDTO solicitud) {
        return metaMapaApiService.crearSolicitudEliminacion(solicitud);
    }

    public SolicitudCambioInputDTO crearSolicitudCambio(Long idHecho, Long idUsuario,SolicitudCambioInputDTO solicitud) {
        return metaMapaApiService.crearSolicitudCambio(idHecho, idUsuario,solicitud);
    }

    public PageSolicitudEliminacionDTO listarSolicitudesEliminacion (int page, int size, EstadoSolicitud estado) {
        return metaMapaApiService.obetnerTodasLasSolicitudesEliminacion( page,  size,  estado);
    }

    public PageSolicitudCambioDTO listarSolicitudesCambio(int page, int size, String resuelta) {
        return metaMapaApiService.obtenerTodasLasSolicitudesCambio(page, size, resuelta);
    }

    public void aceptarSolicitud(Long id) {
        metaMapaApiService.actualizarEstadoSolicitud(id, "APROBADA");
    }

    public void rechazarSolicitud(Long id) {
        metaMapaApiService.actualizarEstadoSolicitud(id, "RECHAZADA");
    }

    public void aceptarSolicitudCambio(Long id, Long idAdmin) {
        metaMapaApiService.actualizarEstadoSolicitudCambio(id, idAdmin, true);
    }

    public void rechazarSolicitudCambio(Long id, Long idAdmin) {
        metaMapaApiService.actualizarEstadoSolicitudCambio(id, idAdmin, false);
    }

    public void marcarComoSpam(Long id) {
        // Antes enviábamos "RECHAZADA", ahora enviamos "SPAM" para usar la 'S' del backend
        metaMapaApiService.actualizarEstadoSolicitud(id, "SPAM");
    }

    }



