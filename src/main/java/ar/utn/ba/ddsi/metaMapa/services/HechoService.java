package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.metaMapa.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class HechoService {

    private final MetaMapaApiService metaMapaApiService;

    public HechoService(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
    }

    public List<HechoDTO> obtenerTodosLosHechos(int page, int limit) {
        return metaMapaApiService.listarHechos(page, limit);
    }

    public List<HechoDTO> obtenerTodosLosHechos(
            int page,
            int limit,
            boolean aplicarFiltros,
            String categoria,
            String provincia,
            String ciudad,
            String localidad,
            String fechaInicio,
            String fechaFin,
            String cargaOrigen,
            String misHechos,
            String busquedaCurada
    ) {
        if (!aplicarFiltros) {
            return metaMapaApiService.listarHechos(page, limit);
        } else {
            return metaMapaApiService.listarHechosFiltrados(
                    page,
                    limit,
                    categoria,
                    provincia,
                    ciudad,
                    localidad,
                    fechaInicio,
                    fechaFin,
                    cargaOrigen,
                    misHechos,
                    busquedaCurada
            );
        }
    }

    public HechoDTO obtenerHecho(long id) {
        HechoDTO hecho = metaMapaApiService.getHechoById(id);
        return  hecho;
    }

    public List<HechoDTO> hechosDestacados() {
        List<HechoDTO> hechoDTOS = metaMapaApiService.obtenerHechosDestacados();
        return hechoDTOS;
    }

    public HechoDTO visualizarHecho(Long id) {
        // Mock simple para vista detalle
        HechoDTO h = new HechoDTO();
        return h;
    }

    public HechoDTO crearHecho(HechoInputDTO hecho) {
        return metaMapaApiService.crearHecho(hecho);
    }
}
