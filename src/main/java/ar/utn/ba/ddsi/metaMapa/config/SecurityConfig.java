package ar.utn.ba.ddsi.metaMapa.config;

import ar.utn.ba.ddsi.metaMapa.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider)
                .build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ⛔ importante: permitir landing y recursos estáticos
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",                      // root
                                "/landingPage",           // landing pública
                                "/home",
                                "/estadisticas",
                                "mapa",
                                "/legal",
                                "solicitudes/**",
                                "formularioSolicitudEliminacion",
                                "formularioSolicitudCambio",
                                "/importarCsv",
                                "enDesarrollo",// si la usás
                                "/login", "/signin",      // vistas de auth
                                "/error", "/403", "/404",
                                "/favicon.ico",
                                "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()

                        // Visualización anónima (Entrega 5): GET a colecciones/hechos sin login
                        .requestMatchers(HttpMethod.GET, "/colecciones/**", "/hechos/**","/coleccion/**").permitAll()

                        // TODO: endpoints públicos adicionales, agregalos arriba

                        .anyRequest().authenticated()
                )

                // Login y logout "clásicos"
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login/submit")
                        .permitAll()
                        .defaultSuccessUrl("/landingPage", true)  // o donde quieras caer
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/landingPage")
                        .permitAll()
                )

                // CSRF: dejalo ON para formularios Thymeleaf. Si tenés APIs, podés ignorar rutas puntuales.
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/signin",                // si haces POST sin token CSRF
                                "/solicitudes/**"         // ejemplo API pública
                        )
                )

                // Manejo de errores de autorización
                .exceptionHandling(ex -> ex
                        // Usuario no autenticado → redirigir a login
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/login?unauthorized")
                        )
                        // Usuario autenticado pero sin permisos → redirigir a página de error
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/403")
                        )
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
