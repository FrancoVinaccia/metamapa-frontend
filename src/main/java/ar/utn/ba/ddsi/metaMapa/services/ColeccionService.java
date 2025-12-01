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

    public ColeccionDTO crearColeccion(ColeccionInputDTO coleccion) {
        return metaMapaApiService.crearColeccion(coleccion);
    }
}