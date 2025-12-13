package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.services.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/estadisticas")
    public String estadisticas(Model model) {
        try {


            model.addAttribute(
                    "categoriaMasHechos",
                    estadisticasService.obtenerCategoriaConMasHechos()
            );


            model.addAttribute(
                    "categoriaProvinciaTop",
                    estadisticasService.obtenerProvinciaTopPorCategoria()
            );


           /* model.addAttribute(
                    "coleccionProvinciaTop",
                    estadisticasService.obtenerProvinciaTopPorColeccion()
            );*/


            model.addAttribute(
                    "solicitudesMes",
                    estadisticasService.obtenerSolicitudesSpamNoSpamDelMes()
            );

            return "Fragments/estadisticas";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute(
                    "errorMensaje",
                    "Ocurrió un error al cargar las estadísticas: " + e.getMessage()
            );
            return "errorGenerico";
        }
    }
}
