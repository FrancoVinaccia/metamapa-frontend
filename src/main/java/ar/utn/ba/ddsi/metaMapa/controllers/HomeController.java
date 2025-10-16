// Ubicación: src/main/java/ar/utn/ba/ddsi/metaMapa/controllers/HomeController.java

package ar.utn.ba.ddsi.metaMapa.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String vacio() {
        return "redirect:/landingPage";
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/solicitudes")
    public String solicitudes() {
        return "Fragments/solicitudes";
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/importarCsv")
    public String importarCsv() {
        return "Fragments/importarCsv";
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

    @GetMapping("/403")
    public String accessDenied(Model model) {
        model.addAttribute("titulo", "Acceso denegado");
        return "403";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/formularioSolicitud")
    public String crearSolicitud() {
        return "Fragments/crearSolicitud"; // CORREGIDO
    }



}