package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.ColeccionDTO;
import ar.utn.ba.ddsi.metaMapa.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

    private final ColeccionService coleccionService;

    @GetMapping
    public String listarColecciones(Model model) {
        List<ColeccionDTO> colecciones = coleccionService.obtenerTodasLasColecciones();
        model.addAttribute("colecciones", colecciones);            // <-- key correcta
        model.addAttribute("titulo", "Lista de Colecciones");      // <-- texto correcto
        model.addAttribute("totalColecciones", colecciones.size()); // <-- contador correcto
        return "coleccion/colecciones"; // templates/coleccion/colecciones.html
    }

    @GetMapping("/{id}")
    public String visualizarColeccion(@PathVariable Long id, Model model) {
        // Si tenés detalle, traer y setear:
         ColeccionDTO c = coleccionService.visualizarColeccion(id.intValue());
         model.addAttribute("coleccion", c);
         model.addAttribute("titulo", c.getTitulo());
         model.addAttribute("hechos", c.getHechos());
        return "coleccion/coleccion";
    }
}
