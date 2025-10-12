package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.services.ColeccionService;
import ar.utn.ba.ddsi.metaMapa.services.HechoService;
import ar.utn.ba.ddsi.metaMapa.services.LandingPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/landingPage")
@RequiredArgsConstructor
public class LandingPageController {

    private final LandingPageService landingPageService;
    private final HechoService servicioHechos;
    private final ColeccionService servicioColecciones;

    @GetMapping
    public String listarAlumnos(Model model) {
        model.addAttribute("titulo", "Landing Page");
        model.addAttribute("hechosDestacados", servicioHechos.hechosDestacados()); // List<Hecho>
        model.addAttribute("coleccionesDestacadas", servicioColecciones.coleccionesDestacadas());
        System.out.println("Entrando a la landing page");
        System.out.println("Hechos destacados: " + servicioHechos.hechosDestacados().size());
        System.out.println("Colecciones destacadas: " + servicioColecciones.coleccionesDestacadas().size());
        return "home/landing";
    }


}