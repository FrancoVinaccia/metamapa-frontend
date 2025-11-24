package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.services.ColeccionService;
import ar.utn.ba.ddsi.metaMapa.services.HechoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/landingPage")
@RequiredArgsConstructor
public class LandingPageController {

    private final HechoService servicioHechos;
    private final ColeccionService servicioColecciones;

    @GetMapping
    public String landingPage(Model model) {
        model.addAttribute("titulo", "Landing Page");
        model.addAttribute("hechosDestacados", servicioHechos.hechosDestacados()); // List<Hecho>
        model.addAttribute("coleccionesDestacadas", servicioColecciones.obtenerTodasLasColecciones(1,3));
        System.out.println("Entrando a la landing page");
        return "home/landing";
    }


}
