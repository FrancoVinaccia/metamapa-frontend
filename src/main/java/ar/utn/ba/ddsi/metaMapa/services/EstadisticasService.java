package ar.utn.ba.ddsi.metaMapa.services;


import ar.utn.ba.ddsi.metaMapa.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadisticasService {

    private final MetaMapaApiService metaMapaApiService;

    public CategoriaTopDTO obtenerCategoriaConMasHechos() {
        return metaMapaApiService.obtenerCategoriaConMasHechos();
    }

    public List<CategoriaProvinciaDTO> obtenerProvinciaTopPorCategoria() {
        List<CategoriaProvinciaDTO> lista = metaMapaApiService.obtenerProvinciaTopPorCategoria();
        lista.sort((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()));
        return lista;
    }

    public List<ColeccionProvinciaDTO> obtenerProvinciaTopPorColeccion() {
        try {
            List<ColeccionProvinciaDTO> lista = metaMapaApiService.obtenerProvinciaTopPorColeccion();
            lista.sort((a,b) -> Integer.compare(b.getCantidad(), a.getCantidad()));
            return lista;
        } catch (Exception e) {
            return List.of();
        }
    }

    public SolicitudesSpamDTO obtenerSolicitudesSpamNoSpamDelMes() {
        return metaMapaApiService.obtenerSolicitudesSpamNoSpamDelMes();
    }

    public List<DiaCategoriaDTO> obtenerDiaCategoria() {
        return metaMapaApiService.obtenerDiaCategoria();
    }

}