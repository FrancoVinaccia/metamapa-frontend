package ar.utn.ba.ddsi.metaMapa.services;


import ar.utn.ba.ddsi.metaMapa.dto.CategoriaProvinciaDTO;
import ar.utn.ba.ddsi.metaMapa.dto.CategoriaTopDTO;
import ar.utn.ba.ddsi.metaMapa.dto.SolicitudesSpamDTO;
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


    public SolicitudesSpamDTO obtenerSolicitudesSpamNoSpamDelMes() {
        return metaMapaApiService.obtenerSolicitudesSpamNoSpamDelMes();
    }
}