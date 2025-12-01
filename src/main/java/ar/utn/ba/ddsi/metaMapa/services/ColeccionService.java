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

        // parsear busquedaCurada a Boolean si viene
        Boolean curada = null;
        if (busquedaCurada != null && !busquedaCurada.isBlank()) {
            curada = Boolean.valueOf(busquedaCurada);
        }

        // intentar parsear id a Long si corresponde (si la API lo espera)
        Long idLong = null;
        try {
            idLong = Long.parseLong(idColeccion);
        } catch (NumberFormatException ignored) {}

        PageHechoDTO hechosPage = metaMapaApiService.buscarHechos(page, limit,
                null, // categoria/tema/ubicacion no usados aquí
                null,
                null,
                null,
                null,
                curada,
                idLong,
                null);

        coleccion.setHechos(hechosPage);
        return coleccion;
    }

    public ColeccionDTO crearColeccion(ColeccionInputDTO coleccion) {
        return metaMapaApiService.crearColeccion(coleccion);
    }
}