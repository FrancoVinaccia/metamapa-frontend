// language: java
package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.LugarDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.HechoInputDTO;
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

@Controller
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping("/solicitudCambio/{idHecho}")
    public String solicitudCambio(@PathVariable("idHecho") Long idHecho, Model model) {
        model.addAttribute("idHecho", idHecho);

        HechoInputDTO hecho = new HechoInputDTO();
        hecho.setLugar(new LugarDTO());
        model.addAttribute("hecho", hecho);

        return "solicitudes/solicitudCambio";
    }

    @PostMapping("/crearSolicitudCambio/{idHecho}")
    public String crearSolicitudCambio(
            @PathVariable("idHecho") Long idHecho,
            @ModelAttribute("solicitudCambio") SolicitudCambioInputDTO solicitud,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            System.out.println(solicitud);
            solicitudService.crearSolicitudCambio(idHecho, solicitud);
            redirectAttributes.addFlashAttribute("success", "Solicitud de cambio creada con éxito.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/hechos";
        } catch (Exception e) {
            model.addAttribute("idHecho", idHecho);
            model.addAttribute("error", "Error al crear la solicitud de cambio: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            return "solicitudes/solicitudCambio";
        }
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE', 'REGISTRADO', 'ADMINISTRADOR')")
    @PostMapping("/solicitudEliminacion")
    public String crearSolicitudEliminar(
            @ModelAttribute("solicitudEliminacion") SolicitudEliminacionInputDTO solicitud,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        try{
            System.out.println("Creando solicitud de eliminacion: " + solicitud);
            solicitudService.crearSolicitudEliminacion(solicitud);
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
}
