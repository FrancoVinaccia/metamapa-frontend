// java
package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.HechoDTO;
import ar.utn.ba.ddsi.metaMapa.dto.SignInRequestDTO;
import ar.utn.ba.ddsi.metaMapa.exceptions.NotFoundException;
import ar.utn.ba.ddsi.metaMapa.dto.AuthResponseDTO;
import ar.utn.ba.ddsi.metaMapa.services.internal.WebApiCallerService;
import ar.utn.ba.ddsi.metaMapa.dto.RolesPermisosDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class MetaMapaApiService {
    private static final Logger log = LoggerFactory.getLogger(MetaMapaApiService.class);
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String dinamicApi;

    @Autowired
    public MetaMapaApiService(
            WebApiCallerService webApiCallerService,
            @Value("${metamapa.dinamica.url}") String dinamicApi) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.dinamicApi = dinamicApi;
    }

    public AuthResponseDTO login(String username, String password) {
        try {
            AuthResponseDTO response = webClient
                    .post()
                    .uri(dinamicApi + "/logIn")
                    .bodyValue(Map.of(
                            "nombreUsuario", username,
                            "contrasenia", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            log.error(e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    public HechoDTO crearHecho(HechoDTO hecho) {
        HechoDTO response = webApiCallerService.post(dinamicApi + "/hechos/new", hecho, HechoDTO.class);
        System.out.println(response);
        if (response == null) {
            throw new RuntimeException("Error al crear hecho en el servicio externo");
        }
        return response;
    }

    public void register(SignInRequestDTO signinRequest) {
        // Construye la URL completa para el endpoint de registro.
        // Asegúrate que el endpoint en tu backend se llame "/register" o ajústalo.
        String registerUrl = dinamicApi + "/signIn";

        // Imprime en consola para depuración (puedes quitarlo después).
        System.out.println("Enviando solicitud de registro a: " + registerUrl);

        webClient.post() // Indica que es una petición POST.
                .uri(registerUrl) // La URL del endpoint.
                .bodyValue(signinRequest) // El cuerpo de la petición será nuestro DTO.
                .retrieve() // Ejecuta la petición.
                .toBodilessEntity() // Espera una respuesta sin cuerpo. Si tu API devuelve algo, esto se puede cambiar.
                .block(); // Espera a que la operación termine.
    }



    /*
    public List<AlumnoDTO> obtenerTodosLosAlumnos() {
        List<AlumnoDTO> response = webApiCallerService.getList(alumnosServiceUrl + "/alumnos", AlumnoDTO.class);
        return response != null ? response : List.of();
    }

    public AlumnoDTO obtenerAlumnoPorLegajo(String legajo) {
        AlumnoDTO response = webApiCallerService.get(alumnosServiceUrl + "/alumnos/" + legajo, AlumnoDTO.class);
        if (response == null) {
            throw new NotFoundException("Alumno", legajo);
        }
        return response;
    }

    public AlumnoDTO crearAlumno(AlumnoDTO alumnoDTO) {
        AlumnoDTO response = webApiCallerService.post(alumnosServiceUrl + "/alumnos", alumnoDTO, AlumnoDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al crear alumno en el servicio externo");
        }
        return response;
    }

    public AlumnoDTO actualizarAlumno(String legajo, AlumnoDTO alumnoDTO) {
        AlumnoDTO response = webApiCallerService.put(alumnosServiceUrl + "/alumnos/" + legajo, alumnoDTO, AlumnoDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al actualizar alumno en el servicio externo");
        }
        return response;
    }

    public void eliminarAlumno(String legajo) {
        webApiCallerService.delete(alumnosServiceUrl + "/alumnos/" + legajo);
    }

    public boolean existeAlumno(String legajo) {
        try {
            obtenerAlumnoPorLegajo(legajo);
            return true;
        } catch (NotFoundException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia del alumno: " + e.getMessage(), e);
        }
    }
    */
}
