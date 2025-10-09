package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.models.dto.ColeccionDTO;
import ar.utn.ba.ddsi.metaMapa.models.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionController {
    private final ColeccionService coleccionService;

    @GetMapping("{id}")
    public String visualizarColeccion() { return "coleccion/coleccion";}
}
