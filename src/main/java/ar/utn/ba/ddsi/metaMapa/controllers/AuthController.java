package ar.utn.ba.ddsi.metaMapa.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;


@Controller
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/login")
    public String login() { return "auth/login"; }

    @GetMapping("/signin")
    public String signin() { return "auth/signin"; }
}