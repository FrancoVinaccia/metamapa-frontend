package ar.utn.ba.ddsi.metaMapa.controllers;


import ar.utn.ba.ddsi.metaMapa.dto.LugarDTO;
import ar.utn.ba.ddsi.metaMapa.dto.SolicitudEliminacionDTO;
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
        // trim y convierte "" en null para TODOS los String de este controller
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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

        System.out.println(">>> ENTRE AL POST /crearSolicitudCambio/" + idHecho);
        System.out.println(">>> DTO RECIBIDO: " + solicitud);

        try {
            System.out.println(">>> ANTES DE LLAMAR AL SERVICE");
            solicitudService.crearSolicitudCambio(idHecho,idUsuario, solicitud);
            System.out.println(">>> DESPUÉS DE LLAMAR AL SERVICE (TODO OK)");

            redirectAttributes.addFlashAttribute("success", "Solicitud de cambio creada con éxito.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

            System.out.println(">>> HAGO REDIRECT A /hechos");
            return "redirect:/hechos";

        } catch (Exception e) {
            System.out.println(">>> ENTRE AL CATCH EN crearSolicitudCambio");
            e.printStackTrace(); // MUY IMPORTANTE

            model.addAttribute("idHecho", idHecho);
            model.addAttribute("error", "Error al crear la solicitud de cambio: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            return "solicitudes/solicitudCambio";
        }
    }

    private void normalizarCamposOpcionales(SolicitudCambioInputDTO solicitud) {
        // Si tenés campos String extras que querés asegurarte que estén en null, podés reforzar acá,
        // pero con el InitBinder ya vienen null si estaban vacíos.

        // Normalizar lugar: si todos sus campos son null, seteamos lugar = null
        if (solicitud.getLugar() != null) {
            LugarDTO lugar = solicitud.getLugar();
            boolean lugarVacio =
                    lugar.getLatitud() == null &&
                            lugar.getLongitud() == null &&
                            esNullOVacio(lugar.getLocalidad()) &&
                            esNullOVacio(lugar.getCiudad()) &&
                            esNullOVacio(lugar.getProvincia());

            if (lugarVacio) {
                solicitud.setLugar(null);
            }
        }
    }

    private boolean esNullOVacio(String s) {
        return s == null || s.isBlank();
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
}
