package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.models.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.services.HechoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechoController {

    private final HechoService hechoService;

    @GetMapping
    public String listarHechos(Model model) {
        List<HechoDTO> hechos = hechoService.obtenerTodosLosHechos();
        model.addAttribute("hechos", hechos);
        model.addAttribute("titulo", "Lista de Hechos");
        model.addAttribute("totalDeHechos", hechos.size());
        return "lista";
    }

    @GetMapping("{id}")
    public String visualizarHecho() { return "hecho/hecho";}

    @GetMapping("hecho/formulario")
    public String formularioCrearHecho() { return "hecho/formularioCreacion";}

    @PostMapping("hecho/crear")
    public String crearHecho() { return "hecho/creacion";}
}
