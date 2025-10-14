package ar.utn.ba.ddsi.metaMapa.providers;

import ar.utn.ba.ddsi.metaMapa.dto.AuthResponseDTO;
import ar.utn.ba.ddsi.metaMapa.dto.Rol;
import ar.utn.ba.ddsi.metaMapa.dto.RolesPermisosDTO;
import ar.utn.ba.ddsi.metaMapa.services.MetaMapaApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final MetaMapaApiService externalAuthService;

    public CustomAuthProvider(MetaMapaApiService externalAuthService) {
        this.externalAuthService = externalAuthService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            // Llamada a servicio externo para obtener tokens
            AuthResponseDTO authResponse = externalAuthService.login(username, password);

            if (authResponse == null) {
                throw new BadCredentialsException("Usuario o contraseña inválidos");
            }

            log.info("Usuario logeado! Configurando variables de sesión");
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            log.info(String.valueOf(authResponse));

            request.getSession().setAttribute("id", authResponse.getId());
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("email", authResponse.getEmail());
            request.getSession().setAttribute("fechaNacimiento", authResponse.getFechaNacimiento());
            request.getSession().setAttribute("accessToken", authResponse.getToken());
            request.getSession().setAttribute("refreshToken", authResponse.getRefreshToken());
            request.getSession().setAttribute("rol", authResponse.getRol());

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + authResponse.getRol().name()));

            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        } catch (RuntimeException e) {
            log.error("Error al llamar al servicio de autenticación:", e);
            throw new BadCredentialsException("Error en el sistema de autenticación: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}