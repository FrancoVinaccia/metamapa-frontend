package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.metaMapa.services.HechoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechoController {

    private final HechoService hechoService;

    @GetMapping
    public String listarHechos(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(required = false) String categoria,
                               @RequestParam(required = false) String provincia,
                               @RequestParam(required = false) String ciudad,
                               @RequestParam(required = false) String localidad,
                               @RequestParam(required = false) String fechaInicio,
                               @RequestParam(required = false) String fechaFin,
                               @RequestParam(required = false) String cargaOrigen,
                               @RequestParam(required = false) String misHechos,
                               @RequestParam(required = false) String busquedaCurada,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            int pageSize = 3;

            // detectar si se aplicó al menos un filtro
            boolean aplicarFiltros =
                    (categoria != null && !categoria.isBlank()) ||
                            (provincia != null && !provincia.isBlank()) ||
                            (ciudad != null && !ciudad.isBlank()) ||
                            (localidad != null && !localidad.isBlank()) ||
                            (fechaInicio != null && !fechaInicio.isBlank()) ||
                            (fechaFin != null && !fechaFin.isBlank()) ||
                            (cargaOrigen != null && !cargaOrigen.isBlank()) ||
                            (misHechos != null && misHechos.equalsIgnoreCase("true")) ||
                            (busquedaCurada != null && busquedaCurada.equalsIgnoreCase("true"));

            List<HechoDTO> hechos = hechoService.obtenerTodosLosHechos(
                    page,
                    pageSize,
                    aplicarFiltros,
                    categoria,
                    provincia,
                    ciudad,
                    localidad,
                    fechaInicio,
                    fechaFin,
                    cargaOrigen,
                    misHechos,
                    busquedaCurada
            );

            model.addAttribute("hechos", hechos);
            model.addAttribute("titulo", "Lista de Hechos");
            model.addAttribute("totalDeHechos", hechos.size());
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", pageSize);

            // opcional: para repintar filtros en la vista
            model.addAttribute("categoria", categoria);
            model.addAttribute("provincia", provincia);
            model.addAttribute("ciudad", ciudad);
            model.addAttribute("localidad", localidad);
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
            model.addAttribute("cargaOrigen", cargaOrigen);
            model.addAttribute("misHechos", misHechos);
            model.addAttribute("busquedaCurada", busquedaCurada);

            return "hecho/hechos";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMensaje", "Ocurrió un error al cargar los hechos: " + e.getMessage());
            return "errorGenerico";
        }
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE', 'REGISTRADO', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    public String visualizarHecho(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
       HechoDTO hecho = hechoService.obtenerHecho(id);
         model.addAttribute("hecho", hecho);
         model.addAttribute("titulo", hecho.getTitulo());
        return "hecho/hecho";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("hecho/formulario")
    public String formularioCrearHecho() { return "hecho/crearHecho";}

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE', 'REGISTRADO', 'ADMINISTRADOR')")
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("hecho", new HechoInputDTO());
        return "hecho/crearHecho";
    }


    @PostMapping("/crear")
    public String crearHecho(
            @ModelAttribute("hecho") HechoInputDTO hecho,
            BindingResult bindingResult,
            @SessionAttribute(value = "id", required = false) Long idUsuario,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Corrija los campos del formulario.");
            model.addAttribute("tipoMensaje", "danger");
            return "hecho/crearHecho";
        }

        try {
            if (idUsuario != null) {
                hecho.setIdUsuario(idUsuario);
            }

            HechoDTO creado = hechoService.crearHecho(hecho);

            // Si la creación fue OK redirigimos al listado y mostramos mensaje
            redirectAttributes.addFlashAttribute("success", "Hecho creado con éxito.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/hechos"; // redirige a la lista de hechos

        } catch (Exception e) {
            // en caso de error mostramos la misma página con el mensaje
            model.addAttribute("error", "Error al crear el hecho: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            return "hecho/crearHecho";
        }
    }


}
