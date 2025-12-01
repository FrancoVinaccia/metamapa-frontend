package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.ColeccionDTO;
import ar.utn.ba.ddsi.metaMapa.dto.RangoFechaDTO;
import ar.utn.ba.ddsi.metaMapa.dto.LugarDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.CriterioPertenenciaInputDTO;
import ar.utn.ba.ddsi.metaMapa.services.ColeccionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

    private final ColeccionService coleccionService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

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
    public String verColeccion(@PathVariable("id") String id,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "busquedaCurada", required = false) String busquedaCurada,
                               Model model) {
        try {
            int pageSize = 9;
            ColeccionDTO coleccion = coleccionService.obtenerColeccionPorId(id, page, pageSize, busquedaCurada);
            if (coleccion == null) {
                model.addAttribute("errorMensaje", "Colección no encontrada");
                return "errorGenerico";
            }
            model.addAttribute("coleccion", coleccion);
            model.addAttribute("currentPage", page);
            return "coleccion/coleccion";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMensaje", "Ocurrió un error al cargar la colección: " + e.getMessage());
            return "errorGenerico";
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/nueva")
    public String mostrarFormularioCrear(Model model) {
        ColeccionInputDTO coleccionDTO = new ColeccionInputDTO();

        // Inicializar objetos anidados para evitar NullPointerException en la vista o el binding
        CriterioPertenenciaInputDTO criterios = new CriterioPertenenciaInputDTO();
        criterios.setFecha(new RangoFechaDTO());
        criterios.setLugar(new LugarDTO());

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
            System.out.println("Coleccion (antes sanitizar) = " + coleccion);

            // Sanitizar antes de enviar al servicio (quita DTOs anidados vacíos)
            sanitizeColeccion(coleccion);

            System.out.println("Coleccion (sanitizada) = " + coleccion);
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

    // Métod privado que normaliza el DTO: quita criterios.fecha y criterios.lugar si están vacíos,
    // y setea criterios a null si queda completamente vacío.
    private void sanitizeColeccion(ColeccionInputDTO coleccion) {
        if (coleccion == null) return;

        CriterioPertenenciaInputDTO criterios = coleccion.getCriterios();
        if (criterios == null) return;

        // Normalizar categoría
        if (criterios.getCategoria() != null && criterios.getCategoria().isBlank()) {
            criterios.setCategoria(null);
        }

        // Fecha: si ambos campos vacíos -> quitar el DTO fecha
        RangoFechaDTO fecha = criterios.getFecha();
        if (fecha != null) {
            String fi = fecha.getFechaInicio();
            String ff = fecha.getFechaFin();
            if ((fi == null || fi.isBlank()) && (ff == null || ff.isBlank())) {
                criterios.setFecha(null);
            }
        }

        // Lugar: si todos los campos relevantes vacíos -> quitar el DTO lugar
        LugarDTO lugar = criterios.getLugar();
        if (lugar != null) {
            String loc = lugar.getLocalidad();
            String ciu = lugar.getCiudad();
            String prov = lugar.getProvincia();
            if ((loc == null || loc.isBlank()) &&
                    (ciu == null || ciu.isBlank()) &&
                    (prov == null || prov.isBlank())) {
                criterios.setLugar(null);
            }
        }

        // Si criterios quedó vacío (sin categoría, sin fecha, sin lugar) -> setear a null
        boolean categoriaVacia = criterios.getCategoria() == null || criterios.getCategoria().isBlank();
        if (categoriaVacia && criterios.getFecha() == null && criterios.getLugar() == null) {
            coleccion.setCriterios(null);
        }
    }
}