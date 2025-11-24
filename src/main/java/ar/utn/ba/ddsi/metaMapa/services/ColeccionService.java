package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.ColeccionDTO;
import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.dto.PageHechoDTO;
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

    public ColeccionDTO visualizarColeccion(Integer id) {
        // Busca por id en el mock y devuelve una copia con su lista de hechos
        return coleccionesMock().stream()
                .filter(c -> c.getId().equals(id.longValue()))
                .findFirst()
                .orElseGet(() -> {
                    // fallback vacío pero con wrapper de hechos inicializado
                    return ColeccionDTO.builder()
                            .id(id.longValue())
                            .titulo("Colección no encontrada")
                            .descripcion("No existen datos para el id " + id)
                            .imagen("https://picsum.photos/seed/notfound/800/400")
                            .hechos(pageFromList(new ArrayList<>()))
                            .build();
                });
    }

    public List<ColeccionDTO> coleccionesDestacadas() {
        return coleccionesMock();
    }

    // ===== MOCK =====
    private List<ColeccionDTO> coleccionesMock() {
        List<ColeccionDTO> list = new ArrayList<>();

        ColeccionDTO c1 = ColeccionDTO.builder()
                .id(1L)
                .titulo("Conflictos Globales")
                .descripcion("Eventos y hitos de conflictos internacionales recientes.")
                .imagen("https://picsum.photos/seed/col-1/800/400")
                .hechos(pageFromList(hechosPara(1)))
                .build();

        ColeccionDTO c2 = ColeccionDTO.builder()
                .id(2L)
                .titulo("Innovación y Ciencia")
                .descripcion("Avances científicos y tecnológicos destacados.")
                .imagen("https://picsum.photos/seed/col-2/800/400")
                .hechos(pageFromList(hechosPara(2)))
                .build();

        ColeccionDTO c3 = ColeccionDTO.builder()
                .id(3L)
                .titulo("Economía y Mercados")
                .descripcion("Cambios macroeconómicos y movimientos de mercado.")
                .imagen("https://picsum.photos/seed/col-3/800/400")
                .hechos(pageFromList(hechosPara(3)))
                .build();

        list.add(c1);
        list.add(c2);
        list.add(c3);
        return list;
    }

    private List<HechoDTO> hechosPara(int coleccionId) {
        List<HechoDTO> hechos = new ArrayList<>();
        // … llenar con objetos de prueba si se desea
        return hechos;
    }

    private PageHechoDTO pageFromList(List<HechoDTO> list) {
        PageHechoDTO page = new PageHechoDTO();
        page.setElementos(list);
        return page;
    }
}