package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.ColeccionDTO;
import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.dto.PageHechoDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.HechoInputDTO;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ColeccionService {

    private final MetaMapaApiService metaMapaApiService;

    public ColeccionService(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
    }

    public List<ColeccionDTO> obtenerTodasLasColecciones(int page, int limit) {
        return metaMapaApiService.listarColecciones(page, limit);
    }

    public ColeccionDTO obtenerColeccionPorId(String idColeccion, int page, int limit, String busquedaCurada) {
        ColeccionDTO coleccion = metaMapaApiService.getColeccionById(idColeccion);
        if (coleccion == null) {
            return null;
        }

        Boolean curada = false;
        if (busquedaCurada != null && !busquedaCurada.isBlank()) {
            curada = Boolean.valueOf(busquedaCurada);
        }

        // Pasar el id de la colección tal cual (String) al buscar hechos
        try {
            PageHechoDTO pageHechos = metaMapaApiService.buscarHechos(
                    page,
                    limit,
                    null,   // tema/etiquetas
                    null,   // ubicacion (ciudad/localidad)
                    null,   // categoria
                    null,   // fuente / cargaOrigen
                    null,   // fecha (fechaInicio)
                    curada, // busquedaCurada
                    idColeccion, // idColeccion como String
                    null    // idUsuario
            );
            if (pageHechos == null) {
                pageHechos = new PageHechoDTO();
            }
            coleccion.setHechos(pageHechos);
        } catch (Exception e) {
            coleccion.setHechos(new PageHechoDTO());
        }

        return coleccion;
    }

    public ColeccionDTO crearColeccion(ColeccionInputDTO coleccion) {
        return metaMapaApiService.crearColeccion(coleccion);
    }

    public void actualizarColeccion(String id, ColeccionInputDTO coleccionInput) {
        metaMapaApiService.actualizarColeccion(id, coleccionInput);
    }
}