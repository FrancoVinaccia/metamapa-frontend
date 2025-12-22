package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.DiaCategoriaDTO;
import ar.utn.ba.ddsi.metaMapa.services.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/estadisticas")
    public String estadisticas(Model model) {
        try {

            model.addAttribute("categoriaMasHechos",
                    estadisticasService.obtenerCategoriaConMasHechos()
            );

            model.addAttribute("categoriaProvinciaTop",  estadisticasService.obtenerProvinciaTopPorCategoria());

            model.addAttribute("coleccionProvinciaTop", estadisticasService.obtenerProvinciaTopPorColeccion());

            // ✅ NUEVO: /dia-categoria
            List<DiaCategoriaDTO> diaCategoria = estadisticasService.obtenerDiaCategoria();

            // Para la tabla: orden por día ascendente
            diaCategoria.sort(Comparator.comparing(DiaCategoriaDTO::getDia));
            model.addAttribute("diaCategoriaStats", diaCategoria);

            // Para la tarjeta: máximo por cantidad
            DiaCategoriaDTO topDia = diaCategoria.stream()
                    .max(Comparator.comparing(DiaCategoriaDTO::getCantidad))
                    .orElse(null);
            model.addAttribute("diaCategoriaTop", topDia);

            model.addAttribute("solicitudesMes", estadisticasService.obtenerSolicitudesSpamNoSpamDelMes()
            );

            return "Fragments/estadisticas";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMensaje",
                    "Ocurrió un error al cargar las estadísticas: " + e.getMessage()
            );
            return "errorGenerico";
        }
    }
}
