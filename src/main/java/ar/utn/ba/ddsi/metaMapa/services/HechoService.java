package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import org.springframework.stereotype.Service;

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
        return hechos.stream().filter(h -> h.getId() == id).findFirst().orElse(null);

    }

    public List<HechoDTO> hechosDestacados() {
        HechoDTO h1 = new HechoDTO();
        h1.setId(1);
        h1.setTitulo("Hecho mock 1");
        h1.setDescripcion("Descripción mock 1");
        h1.setImagen("https://picsum.photos/seed/hecho-1/800/400"); // <-- imagen

        HechoDTO h2 = new HechoDTO();
        h2.setId(2);
        h2.setTitulo("Hecho mock 2");
        h2.setDescripcion("Descripción mock 2");
        h2.setImagen("https://picsum.photos/seed/hecho-2/800/400"); // <-- imagen

        return List.of(h1, h2);
    }

    public HechoDTO visualizarHecho(Long id) {
        // Mock simple para vista detalle
        HechoDTO h = new HechoDTO();
        h.setId(id != null ? id.intValue() : 0);
        h.setTitulo("Detalle de Hecho " + h.getId());
        h.setDescripcion("Descripción mock para el hecho " + h.getId());
        h.setImagen("https://picsum.photos/seed/hecho-" + h.getId() + "/1200/600");
        return h;
    }

    public HechoDTO crearHecho(HechoDTO hecho) {
        return metaMapaApiService.crearHecho(hecho);
    }
}
