package ar.utn.ba.ddsi.metaMapa.controllers;

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

    @GetMapping
    public String listarAlumnos(Model model) {
        model.addAttribute("titulo", "Landing Page");
        return "home/landing";
    }
}