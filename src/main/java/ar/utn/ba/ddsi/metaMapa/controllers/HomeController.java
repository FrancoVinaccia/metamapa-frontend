package ar.utn.ba.ddsi.metaMapa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String vacio() {
        return "redirect:/landingPage";
    }

    @GetMapping("/estadisticas")
    public String stats() {
        return "Fragments/estadisticas";
    }

    @GetMapping("/solicitudes")
    public String solicitudes() {
        return "Fragments/solicitudes";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/landingPage";
    }

    @GetMapping("/mapa")
    public String mapa() {
        return "home/mapa";
    }

    @GetMapping("/legal")
    public String legal(Model model) {
        model.addAttribute("titulo", "Información legal y privacidad");
        return "home/legal";
    }

    @GetMapping("/enDesarrollo")
    public String enDesarrollo(Model model) {
        model.addAttribute("titulo", "Funcionalidad en desarrollo");
        return "home/enDesarrollo";
    }

    @GetMapping("/404")
    public String notFound(Model model) {
        model.addAttribute("titulo", "No encontrado");
        return "404";
    }
}
