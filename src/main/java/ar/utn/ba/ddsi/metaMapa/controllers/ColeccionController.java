// java
// File: src/main/java/ar/utn/ba/ddsi/metaMapa/controllers/ColeccionController.java
package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.ColeccionDTO;
import ar.utn.ba.ddsi.metaMapa.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

    private final ColeccionService coleccionService;

    @GetMapping
    public String listarColecciones(@RequestParam(value = "page", defaultValue = "1") int page,
                                    Model model) {
        try {
            int pageSize = 3;
            List<ColeccionDTO> colecciones = coleccionService.obtenerTodasLasColecciones(page, pageSize);
            model.addAttribute("colecciones", colecciones);
            model.addAttribute("titulo", "Lista de Colecciones");
            model.addAttribute("totalColecciones", colecciones.size());
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", pageSize);
            return "coleccion/colecciones";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMensaje", "Ocurrió un error al cargar las colecciones: " + e.getMessage());
            return "errorGenerico";
        }
    }

    @GetMapping("/{id}")
    public String visualizarColeccion(@PathVariable Long id, Model model) {
        ColeccionDTO c = coleccionService.visualizarColeccion(id.intValue());
        model.addAttribute("coleccion", c);
        model.addAttribute("titulo", c.getTitulo());
        // Pasar la lista plana de hechos a la vista
        model.addAttribute("hechos", c.getHechosLista());
        return "coleccion/coleccion";
    }
}
