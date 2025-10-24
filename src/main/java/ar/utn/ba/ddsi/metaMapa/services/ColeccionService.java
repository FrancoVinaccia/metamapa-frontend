package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.ColeccionDTO;
import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColeccionService {

    public List<ColeccionDTO> obtenerTodasLasColecciones() {
        return coleccionesMock();
    }

    public ColeccionDTO visualizarColeccion(Integer id) {
        // Busca por id en el mock y devuelve una copia con su lista de hechos
        return coleccionesMock().stream()
                .filter(c -> c.getId().equals(id.longValue()))
                .findFirst()
                .orElseGet(() -> {
                    // fallback vacío pero con lista inicializada
                    return ColeccionDTO.builder()
                            .id(id.longValue())
                            .titulo("Colección no encontrada")
                            .descripcion("No existen datos para el id " + id)
                            .imagen("https://picsum.photos/seed/notfound/800/400")
                            .hechos(new ArrayList<>())
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
                .hechos(hechosPara(1))
                .build();

        ColeccionDTO c2 = ColeccionDTO.builder()
                .id(2L)
                .titulo("Innovación y Ciencia")
                .descripcion("Avances científicos y tecnológicos destacados.")
                .imagen("https://picsum.photos/seed/col-2/800/400")
                .hechos(hechosPara(2))
                .build();

        ColeccionDTO c3 = ColeccionDTO.builder()
                .id(3L)
                .titulo("Economía y Mercados")
                .descripcion("Cambios macroeconómicos y movimientos de mercado.")
                .imagen("https://picsum.photos/seed/col-3/800/400")
                .hechos(hechosPara(3))
                .build();

        list.add(c1);
        list.add(c2);
        list.add(c3);
        return list;
    }

    private List<HechoDTO> hechosPara(int coleccionId) {
        List<HechoDTO> hechos = new ArrayList<>();
        if (coleccionId == 1) {
            hechos.add(HechoDTO.builder()
                    .id(101)
                    .titulo("Acuerdo de alto el fuego")
                    .descripcion("Partes firman cese de hostilidades supervisado por la ONU.")
                    .imagen("https://picsum.photos/seed/h-101/600/300")
                    .categoria("Política Internacional")
                    .ubicacion("Ginebra, Suiza")
                    .build());
            hechos.add(HechoDTO.builder()
                    .id(102)
                    .titulo("Negociaciones multilaterales")
                    .descripcion("Nuevas rondas de diálogo con mediadores internacionales.")
                    .imagen("https://picsum.photos/seed/h-102/600/300")
                    .categoria("Diplomacia")
                    .ubicacion("Bruselas, Bélgica")
                    .build());
        }
        // … idem para coleccionId 2 y 3
        return hechos;
    }

}
