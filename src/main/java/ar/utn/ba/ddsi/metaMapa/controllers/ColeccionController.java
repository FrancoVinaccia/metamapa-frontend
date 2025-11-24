// java
// File: src/main/java/ar/utn/ba/ddsi/metaMapa/controllers/ColeccionController.java
package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.ColeccionDTO;
import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.CriterioPertenenciaInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.metaMapa.services.ColeccionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

//    @GetMapping("/{id}")
//    public String visualizarColeccion(@PathVariable Long id, Model model) {
//        ColeccionDTO c = coleccionService.visualizarColeccion(id.intValue());
//        model.addAttribute("coleccion", c);
//        model.addAttribute("titulo", c.getTitulo());
//        // Pasar la lista plana de hechos a la vista
//        model.addAttribute("hechos", c.getHechosLista());
//        return "coleccion/coleccion";
//    }

// Archivo: ColeccionController.java

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/nueva")
    public String mostrarFormularioCrear(Model model) {
        ColeccionInputDTO coleccionDTO = new ColeccionInputDTO();

        // Inicializar objetos anidados para evitar NullPointerException en la vista o el binding
        CriterioPertenenciaInputDTO criterios = new CriterioPertenenciaInputDTO();
        criterios.setFecha(new ar.utn.ba.ddsi.metaMapa.dto.RangoFechaDTO());
        criterios.setLugar(new ar.utn.ba.ddsi.metaMapa.dto.LugarDTO());

        coleccionDTO.setCriterios(criterios);

        model.addAttribute("coleccion", coleccionDTO);
        return "coleccion/crearColeccion";
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PostMapping("/crear")
    public String crearHecho(@ModelAttribute("coleccion") ColeccionInputDTO coleccion,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
        try {
            System.out.println(coleccion);
            ColeccionDTO coleccionCreada = coleccionService.crearColeccion(coleccion);
            System.out.println(coleccionCreada);
            redirectAttributes.addFlashAttribute("success", "Colección creada con éxito.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            // Redirigir para evitar reenvío de formulario y mostrar la lista
            return "redirect:/colecciones";
        } catch (Exception e) {
            e.printStackTrace(); // para ver el stacktrace en logs
            model.addAttribute("error", "Error al crear la colección: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            return "coleccion/crearColeccion";
        }
    }
}