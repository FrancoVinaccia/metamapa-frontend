package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.models.dto.ColeccionDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColeccionService {

    public List<ColeccionDTO> obtenerTodasLasColecciones() {
        return coleccionesMock();
    }

    public ColeccionDTO visualizarColeccion(Integer id) {
        // Mock simple
        ColeccionDTO c = new ColeccionDTO();
        return c;
    }

    public List<ColeccionDTO> coleccionesDestacadas() {
        return coleccionesMock();
    }

    // ===== MOCK =====
    private List<ColeccionDTO> coleccionesMock() {
        List<ColeccionDTO> list = new ArrayList<>();

        ColeccionDTO c1 = new ColeccionDTO();
        c1.setId(1L);
        c1.setTitulo("Conflictos Globales");
        c1.setDescripcion("Eventos y hitos de conflictos internacionales recientes.");
        c1.setImagen("https://picsum.photos/seed/col-1/800/400");

        ColeccionDTO c2 = new ColeccionDTO();
        c2.setId(2L);
        c2.setTitulo("Innovación y Ciencia");
        c2.setDescripcion("Avances científicos y tecnológicos destacados.");
        c2.setImagen("https://picsum.photos/seed/col-2/800/400");

        ColeccionDTO c3 = new ColeccionDTO();
        c3.setId(3L);
        c3.setTitulo("Economía y Mercados");
        c3.setDescripcion("Cambios macroeconómicos y movimientos de mercado.");
        c3.setImagen("https://picsum.photos/seed/col-3/800/400");

        list.add(c1);
        list.add(c2);
        list.add(c3);
        return list;
    }
}
