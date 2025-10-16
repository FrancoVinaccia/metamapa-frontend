// Ubicación: src/main/java/ar/utn/ba/ddsi/metaMapa/controllers/HomeController.java

package ar.utn.ba.ddsi.metaMapa.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String vacio() {
        return "redirect:/landingPage";
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/estadisticas")
    public String stats() {
        return "estadisticas"; // CORREGIDO
    }

    @GetMapping("/solicitudes")
    public String solicitudes() {
        return "solicitudes"; // CORREGIDO
    }

    @GetMapping("/mapa")
    public String mapa() {
        return "home/mapa"; // Asegúrate que exista templates/home/mapa.html
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

    @GetMapping("/403")
    public String accessDenied(Model model) {
        model.addAttribute("titulo", "Acceso denegado");
        return "403";
    }
}