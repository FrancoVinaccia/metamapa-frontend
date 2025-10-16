package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.services.HechoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "hecho/hechos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {

        return "hecho/crearHecho";
    }

    @GetMapping("/{id}")
    public String visualizarHecho(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
       HechoDTO hecho = hechoService.obtenerHecho(id);
         model.addAttribute("hecho", hecho);
         model.addAttribute("titulo", hecho.getTitulo());
        return "hecho/hecho";
    }

    @GetMapping("hecho/formulario")
    public String formularioCrearHecho() { return "hecho/crearHecho";}

    @PostMapping("hecho/crear")
    public String crearHecho() { return "hecho/creacion";}


}
