package ar.utn.ba.ddsi.metaMapa.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class EstadisticasController {

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/estadisticas")
    public String stats() {
        return "Fragments/estadisticas";
    }
}
