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
    public String listarHechos(Model model, RedirectAttributes redirectAttributes) {
        try {
            List<HechoDTO> hechos = hechoService.obtenerTodosLosHechos();
            model.addAttribute("hechos", hechos);
            model.addAttribute("titulo", "Lista de Hechos");
            model.addAttribute("totalDeHechos", hechos.size());
            return "hecho/hechos"; // Vista de éxito
        } catch (Exception e) {
            // MUY IMPORTANTE: Imprime el stack trace para ver la causa real
            e.printStackTrace();

            // Redirige al usuario a una página de error conocida (ej: tu /404 o /enDesarrollo si es temporal)
            // O, simplemente devuelve un error interno (aunque no es ideal para el usuario final)
            model.addAttribute("errorMensaje", "Ocurrió un error al cargar los hechos: " + e.getMessage());
            return "errorGenerico"; // Asume que tienes una vista de error genérica

            // Alternativamente, puedes devolver una página en desarrollo (como ya tienes):
            // return "redirect:/enDesarrollo";
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


    @PreAuthorize("hasAnyRole('CONTRIBUYENTE', 'REGISTRADO', 'ADMINISTRADOR')")
    @PostMapping("/crear")
    public String crearHecho(@ModelAttribute("hecho") HechoInputDTO hecho,
                             @SessionAttribute(value = "id") Long usuarioId,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try{
            hecho.setIdUsuario(usuarioId);
            System.out.println(hecho);
            HechoDTO hechoCreado = hechoService.crearHecho(hecho);
            System.out.println(hechoCreado);
            redirectAttributes.addFlashAttribute("success", "Hecho creado con éxito.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "hecho/crearHecho";
        }
        catch (Exception e){
            model.addAttribute("error", "Error al crear el hecho: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            return "hecho/crearHecho";
        }
    }
}
