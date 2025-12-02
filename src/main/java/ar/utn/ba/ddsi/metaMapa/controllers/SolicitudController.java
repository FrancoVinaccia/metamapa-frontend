package ar.utn.ba.ddsi.metaMapa.controllers;


import ar.utn.ba.ddsi.metaMapa.dto.*;
import ar.utn.ba.ddsi.metaMapa.dto.input.SolicitudCambioInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.SolicitudEliminacionInputDTO;
import ar.utn.ba.ddsi.metaMapa.services.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

@Controller
@RequestMapping("/solicitudes")
@RequiredArgsConstructor

public class SolicitudController {

    private final SolicitudService solicitudService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/solicitudes")
    public String solicitudes() {
        // Default: siempre mostrar primero las de eliminación
        return "redirect:/solicitudes/eliminacion";
    }

    @GetMapping("/eliminacion")
    public String listarSolicitudesEliminacion(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(required = false) EstadoSolicitud estadoSolicitud,
            Model model) {

        try {
            int pageSize = 5;

            PageSolicitudEliminacionDTO pagina =  solicitudService.listarSolicitudesEliminacion(page, pageSize, estadoSolicitud);

            // pestaña activa
            model.addAttribute("activeTab", "ELIMINACION");

            // lista de solicitudes para iterar en el HTML
            model.addAttribute("solicitudesEliminacion", pagina.getElementos());

            // datos de paginación para la vista
            model.addAttribute("currentPage", page);                    // UI: 1-based
            model.addAttribute("totalPages", pagina.getTotalPages());
            model.addAttribute("totalElements", pagina.getTotalElements());

            model.addAttribute("titulo", "Lista de Solicitudes de Eliminación");
            model.addAttribute("estadoSolicitud", estadoSolicitud);

            return "solicitudes/solicitudes";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMensaje", "Ocurrió un error al cargar las solicitudes: " + e.getMessage());
            return "errorGenerico";
        }
    }


    @GetMapping("/cambio")
    public String listarSolicitudesCambio(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "estadoSolicitud", required = false) String estadoSolicitud,
            Model model) {

        try {
            int pageSize = 5;

            System.out.println("Filtro resuelta = " + estadoSolicitud);

            PageSolicitudCambioDTO pagina = solicitudService.listarSolicitudesCambio(page, pageSize, estadoSolicitud);

            model.addAttribute("activeTab", "CAMBIO");
            model.addAttribute("solicitudesCambio", pagina.getElementos());

            // Atributos genéricos (compatibilidad)
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pagina.getTotalPages());
            model.addAttribute("totalElements", pagina.getTotalElements());

            // Atributos específicos que usa la vista de CAMBIO
            model.addAttribute("currentPageCambio", page);
            model.addAttribute("totalPagesCambio", pagina.getTotalPages());
            model.addAttribute("totalElementsCambio", pagina.getTotalElements());

            // Exponer el parámetro para que el select y los enlaces funcionen
            model.addAttribute("estadoSolicitud", estadoSolicitud);

            return "solicitudes/solicitudes";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMensaje",
                    "Ocurrió un error al cargar las solicitudes de cambio: " + e.getMessage());
            return "errorGenerico";
        }
    }


    @GetMapping("/solicitudCambio/{idHecho}")
    public String solicitudCambio(
            @PathVariable("idHecho") Long idHecho, Model model)
    {
        model.addAttribute("idHecho", idHecho);

        SolicitudCambioInputDTO solicitudCambio = new SolicitudCambioInputDTO();
        solicitudCambio.setLugar(new LugarDTO());

        model.addAttribute("solicitudCambio", solicitudCambio);

        return "solicitudes/solicitudCambio";
    }

    @PostMapping("/crearSolicitudCambio/{idHecho}")
    public String crearSolicitudCambio(
            @PathVariable("idHecho") Long idHecho,
            @ModelAttribute("solicitudCambio") SolicitudCambioInputDTO solicitud,
            @SessionAttribute(value = "id") Long idUsuario,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {

            solicitudService.crearSolicitudCambio(idHecho,idUsuario, solicitud);


            redirectAttributes.addFlashAttribute("success", "Solicitud de cambio creada con éxito.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

            return "redirect:/hechos";

        } catch (Exception e) {
            e.printStackTrace(); // MUY IMPORTANTE

            model.addAttribute("idHecho", idHecho);
            model.addAttribute("error", "Error al crear la solicitud de cambio: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            return "solicitudes/solicitudCambio";
        }
    }

    @PostMapping("/solicitudEliminacion")
    public String crearSolicitudEliminar(
            @ModelAttribute("solicitudEliminacion") SolicitudEliminacionInputDTO solicitud,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        try{
            System.out.println("Creando solicitud de eliminacion: " + solicitud);
            SolicitudEliminacionDTO solicitudCreada = solicitudService.crearSolicitudEliminacion(solicitud);
            redirectAttributes.addFlashAttribute("success", "Solicitud creada con éxito.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/hechos";
        }
        catch (Exception e){
            model.addAttribute("error", "Error al crear el hecho: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            return "redirect:/hechos";
        }


    }


   @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping ("/{id}/aceptar")
    public String aceptarSolicitud(@PathVariable("id") Long idSolicitud,
                                   RedirectAttributes redirectAttributes) {
        try {
            solicitudService.aceptarSolicitud(idSolicitud);
            redirectAttributes.addFlashAttribute("success", "Solicitud ACEPTADA. El hecho ha sido eliminado del mapa.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al aprobar: " + e.getMessage());
        }
        return "redirect:/solicitudes/eliminacion";
    }


   @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping ("/{id}/rechazar")
    public String rechazarSolicitud(@PathVariable("id") Long idSolicitud,
                                    RedirectAttributes redirectAttributes) {
        try {
            solicitudService.rechazarSolicitud(idSolicitud);
            redirectAttributes.addFlashAttribute("success", "Solicitud RECHAZADA. El hecho permanece visible.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al rechazar: " + e.getMessage());
        }
        return "redirect:/solicitudes/eliminacion";
    }


    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping ("/{id}/spam")
    public String marcarComoSpam(@PathVariable("id") Long idSolicitud,
                                 RedirectAttributes redirectAttributes) {
        try {
            solicitudService.marcarComoSpam(idSolicitud);
            redirectAttributes.addFlashAttribute("warning", "Solicitud marcada como SPAM y rechazada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al marcar spam: " + e.getMessage());
        }
        return "redirect:/solicitudes/eliminacion";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/cambio/{id}/aceptar")
    public String aceptarSolicitudCambio(@PathVariable("id") Long idSolicitud,
                                         @SessionAttribute(value = "id") Long idAdmin,
                                         RedirectAttributes redirectAttributes) {
        try {
            solicitudService.aceptarSolicitudCambio(idSolicitud, idAdmin);
            redirectAttributes.addFlashAttribute("success", "Solicitud de cambio ACEPTADA.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al aprobar solicitud de cambio: " + e.getMessage());
        }
        return "redirect:/solicitudes/cambio";
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/cambio/{id}/rechazar")
    public String rechazarSolicitudCambio(@PathVariable("id") Long idSolicitud,
                                          @SessionAttribute(value = "id") Long idAdmin,
                                          RedirectAttributes redirectAttributes) {
        try {
            solicitudService.rechazarSolicitudCambio(idSolicitud, idAdmin);
            redirectAttributes.addFlashAttribute("success", "Solicitud de cambio RECHAZADA.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al rechazar solicitud de cambio: " + e.getMessage());
        }
        return "redirect:/solicitudes/cambio";
    }
}


