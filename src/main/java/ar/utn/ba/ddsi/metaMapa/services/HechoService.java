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

    public List<HechoDTO> obtenerTodosLosHechos() {
        // Si querés, podés reutilizar el mock de destacados acá
        return hechosDestacados();
    }

    public HechoDTO obtenerHecho(long id) {
        // Mock simple para vista detalle
        List <HechoDTO> hechos = hechosDestacados();
        return hechos.stream().filter(h -> h.getIdHecho() == id).findFirst().orElse(null);

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
