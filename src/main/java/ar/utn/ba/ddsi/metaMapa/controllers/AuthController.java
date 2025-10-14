
package ar.utn.ba.ddsi.metaMapa.controllers;

import ar.utn.ba.ddsi.metaMapa.dto.SignInRequestDTO;
import ar.utn.ba.ddsi.metaMapa.services.MetaMapaApiService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final MetaMapaApiService metaMapaApiService;

    // Inyectamos el servicio para poder usarlo en este controlador.
    public AuthController(MetaMapaApiService metaMapaApiService) {
        this.metaMapaApiService = metaMapaApiService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signin")
    public String signin() {
        return "auth/signin";
    }

    // Este es el método que procesa el formulario de registro.
    @PostMapping("/signin/submit")
    public String processSignin(@ModelAttribute SignInRequestDTO signinRequest) {
        try {
            // 1. Llama al servicio para registrar al usuario.
            metaMapaApiService.register(signinRequest);

            // 2. Si tiene éxito, redirige al login con un mensaje.
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            // 3. Si falla, redirige de vuelta al registro con un mensaje de error.
            System.err.println("Error durante el registro: " + e.getMessage());
            return "redirect:/signin?error=true";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "colecciones";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}