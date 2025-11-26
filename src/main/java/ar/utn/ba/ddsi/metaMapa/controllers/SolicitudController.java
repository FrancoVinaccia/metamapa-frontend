package ar.utn.ba.ddsi.metaMapa.controllers;


import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.metaMapa.services.HechoService;
import ar.utn.ba.ddsi.metaMapa.services.SolicitudService;
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
@RequestMapping("/solicitudes")
@RequiredArgsConstructor

public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping("/solicitudCambio")
    public String solicitudCambio(@ModelAttribute("hecho") HechoInputDTO hecho) {
        return "solicitudes/solicitudCambio";
    }
}
