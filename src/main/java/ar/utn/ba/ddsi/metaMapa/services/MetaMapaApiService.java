// java
package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.dto.*;
import ar.utn.ba.ddsi.metaMapa.dto.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.SolicitudCambioInputDTO;
import ar.utn.ba.ddsi.metaMapa.dto.input.SolicitudEliminacionInputDTO;
import ar.utn.ba.ddsi.metaMapa.services.internal.WebApiCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class MetaMapaApiService {
    private static final Logger log = LoggerFactory.getLogger(MetaMapaApiService.class);
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String agregacionApi;
    private final String dinamicApi;

    @Autowired
    public MetaMapaApiService(
            WebApiCallerService webApiCallerService,
            @Value("${metamapa.dinamica.url}") String dinamicApi,
            @Value("${metamapa.agregacion.url}") String agregacionApi) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.agregacionApi = agregacionApi;
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


            System.out.println(username);
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

    public List<ColeccionDTO> listarColecciones(int page, int limit) {
        try {
            PageColeccionDTO response = webApiCallerService.get(
                    agregacionApi + "/colecciones/destacadas?page=" + page + "&limit=" + limit,
                    PageColeccionDTO.class
            );
            return response != null && response.getElementos() != null ? response.getElementos() : List.of();
        } catch (Exception e) {
            log.error("Error listando colecciones desde agregacion: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public List<HechoDTO> listarHechos(int page, int limit) {
        try {
            PageHechoDTO response = webApiCallerService.get(
                    agregacionApi + "/hechos/destacados?page=" + page + "&limit=" + limit,
                    PageHechoDTO.class
            );
            return response != null && response.getElementos() != null ? response.getElementos() : List.of();
        } catch (Exception e) {
            log.error("Error listando hechos desde agregacion: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public List<HechoDTO> listarHechosFiltrados(
            int page,
            int limit,
            String categoria,
            String provincia,
            String ciudad,
            String localidad,
            String fechaInicio,
            String fechaFin,
            String cargaOrigen,
            String misHechos
    ) {
        try {
            // base: http://localhost:8080/agre/hechos?page=...&limit=...
            String url = agregacionApi
                    + "/hechos?page="
                    + page
                    + "&limit="
                    + limit;

            if (categoria != null && !categoria.isBlank()) {
                url += "&categoria=" + URLEncoder.encode(categoria, StandardCharsets.UTF_8);
            }
            if (provincia != null && !provincia.isBlank()) {
                url += "&provincia=" + URLEncoder.encode(provincia, StandardCharsets.UTF_8);
            }
            if (ciudad != null && !ciudad.isBlank()) {
                url += "&ciudad=" + URLEncoder.encode(ciudad, StandardCharsets.UTF_8);
            }
            if (localidad != null && !localidad.isBlank()) {
                url += "&localidad=" + URLEncoder.encode(localidad, StandardCharsets.UTF_8);
            }
            if (fechaInicio != null && !fechaInicio.isBlank()) {
                url += "&fechaInicio=" + URLEncoder.encode(fechaInicio, StandardCharsets.UTF_8);
            }
            if (fechaFin != null && !fechaFin.isBlank()) {
                url += "&fechaFin=" + URLEncoder.encode(fechaFin, StandardCharsets.UTF_8);
            }
            if (cargaOrigen != null && !cargaOrigen.isBlank()) {
                url += "&cargaOrigen=" + URLEncoder.encode(cargaOrigen, StandardCharsets.UTF_8);
            }

            // si querés mapear "misHechos" al parámetro del back `busquedaCurada`
            if (misHechos != null && misHechos.equalsIgnoreCase("true")) {
                url += "&busquedaCurada=true";
            }

            System.out.println(">>> URL agregación (filtrados): " + url);

            PageHechoDTO response = webApiCallerService.get(url, PageHechoDTO.class);
            return response != null && response.getElementos() != null ? response.getElementos() : List.of();

        } catch (Exception e) {
            log.error("Error listando hechos filtrados desde agregacion: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public List<HechoDTO> obtenerHechosDestacados() {
        // La URL correcta usa ? para el primer parámetro y & para el segundo.
        PageHechoDTO response = webApiCallerService.get(agregacionApi + "/hechos/destacados?page=1&limit=3", PageHechoDTO.class);
        return response.getElementos();
    }

    public SolicitudEliminacionDTO crearSolicitudEliminacion(SolicitudEliminacionInputDTO solicitud) {
        System.out.println("sssssss");
        SolicitudEliminacionDTO response = webApiCallerService.post(agregacionApi + "/solicitudes/new", solicitud, SolicitudEliminacionDTO.class);
        System.out.println(response);
        if (response == null) {
            throw new RuntimeException("Error al crear solicitud de eliminacion en el servicio externo");
        }
        return response;
    }

    public HechoDTO crearHecho(HechoInputDTO hecho) {
        System.out.println("sssssss");
        HechoDTO response = webApiCallerService.post(dinamicApi + "/hechos/new", hecho, HechoDTO.class);
        System.out.println(response);
        if (response == null) {
            throw new RuntimeException("Error al crear hecho en el servicio externo");
        }
        return response;
    }

    public SolicitudCambioInputDTO crearSolicitudCambio(Long idHecho, SolicitudCambioInputDTO solicitud) {
        String url = dinamicApi + "/hechos/" + idHecho;
        SolicitudCambioInputDTO response = webApiCallerService.post(url, solicitud, SolicitudCambioInputDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al crear solicitud de cambio en el servicio externo");
        }
        return response;
    }

    public ColeccionDTO crearColeccion(ColeccionInputDTO coleccion) {
        System.out.println("2");

        ColeccionDTO response = webApiCallerService.postAdmin(
                agregacionApi + "/priv/colecciones/new",
                coleccion,
                ColeccionDTO.class
        );
        System.out.println("3");
        if (response == null) {
            System.out.println("ERROR");
            throw new RuntimeException("Error al crear coleccion en el servicio externo");
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




    public PageHechoDTO buscarHechos(int page, int limit,
                                     String tema,       // etiquetas
                                     String ubicacion,  // ciudad/localidad
                                     String categoria,
                                     String fuente,     // cargaOrigen
                                     String fecha,      // fechaInicio
                                     Boolean busquedaCurada,
                                     Long idColeccion,  // Para filtrar por colección
                                     Long idUsuario) {  // Para filtrar 'Mis Hechos'

        try {
            // Usamos UriComponentsBuilder para armar la URL con parámetros opcionales
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(agregacionApi + "/hechos")
                    .queryParam("page", page)
                    .queryParam("limit", limit);

            // Agregamos filtros solo si no son nulos/vacíos
            if (idColeccion != null) builder.queryParam("idColeccion", idColeccion);

            // Mapeo de tus filtros del front a los del back (PublicHechoController)
            if (categoria != null && !categoria.isEmpty()) builder.queryParam("categoria", categoria);
            if (ubicacion != null && !ubicacion.isEmpty()) builder.queryParam("ciudad", ubicacion); // Asumiendo que ubicación es ciudad
            if (fuente != null && !fuente.isEmpty()) builder.queryParam("cargaOrigen", fuente);

            // La fecha del front suele ser un día específico. El back espera rango o inicio.
            // Lo mandamos como fechaInicio
            if (fecha != null && !fecha.isEmpty()) builder.queryParam("fechaInicio", fecha);

            if (busquedaCurada != null) builder.queryParam("busquedaCurada", busquedaCurada);

            // Nota: El backend NO parece tener filtro por 'idUsuario' o 'tema' (etiquetas) en PublicHechoController.
            // Esos dos quizás tengamos que seguir filtrándolos en memoria o agregarlos al back después.
            // Por ahora, pedimos los datos filtrados al back y refinamos lo que falte.

            String url = builder.toUriString();
            System.out.println("Llamando a API Externa: " + url);

            return webApiCallerService.get(url, PageHechoDTO.class);

        } catch (Exception e) {
            System.err.println("Error buscando hechos en API: " + e.getMessage());
            return new PageHechoDTO(); // Retorno vacío seguro
        }
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
