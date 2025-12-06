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


import java.net.URI;
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

    public ColeccionDTO getColeccionById(String idColeccion) {
        try {
            String url = agregacionApi + "/colecciones/" + idColeccion;
            return webApiCallerService.get(url, ColeccionDTO.class);
        } catch (Exception e) {
            log.error("Error obteniendo coleccion {} desde agregacion: {}", idColeccion, e.getMessage(), e);
            return null;
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
            String misHechos,
            String busquedaCurada
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

            if (busquedaCurada != null && busquedaCurada.equalsIgnoreCase("true")) {
                if (!url.contains("busquedaCurada")) {
                    url += "&busquedaCurada=true";
                }
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
        PageHechoDTO response = webApiCallerService.get(agregacionApi + "/hechos/destacados?page=1&limit=3", PageHechoDTO.class);
        return response.getElementos();
    }

    public SolicitudEliminacionDTO crearSolicitudEliminacion(SolicitudEliminacionInputDTO solicitud) {
        System.out.println("Solicitud eliminacion: " + solicitud);

        SolicitudEliminacionDTO response = webApiCallerService.post(agregacionApi + "/solicitudes/new", solicitud, SolicitudEliminacionDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al crear solicitud de eliminacion en el servicio externo");
        }
        return response;
    }

    public SolicitudCambioInputDTO crearSolicitudCambio(Long idHecho,Long idUsuario, SolicitudCambioInputDTO solicitud) {
        System.out.println("Solicitud: " + solicitud);
        System.out.println("id usuario: " + idUsuario);
        System.out.println("id hecho: " + idHecho);



        String url = dinamicApi + "/hechos/" + idHecho + "?idUsuario=" + idUsuario;
        SolicitudCambioInputDTO response = webApiCallerService.post(url, solicitud, SolicitudCambioInputDTO.class);
        System.out.println("RT: " + response);

        if (response == null) {
            throw new RuntimeException("Error al crear solicitud de cambio en el servicio externo");
        }
        return response;
    }

    public PageSolicitudEliminacionDTO obetnerTodasLasSolicitudesEliminacion(int page, int limit, EstadoSolicitud estado) {

        int pageBackend = page;


        String url;
        if (estado != null) {
            String estadoParam = estado.name();
            url = agregacionApi + "/priv/solicitudes?page=" + pageBackend + "&limit=" + limit + "&estado=" + estadoParam;
        } else {
            url = agregacionApi + "/priv/solicitudes?page=" + pageBackend + "&limit=" + limit;
        }

        PageSolicitudEliminacionDTO response =
                webApiCallerService.getAdmin(url, PageSolicitudEliminacionDTO.class);

        if (response == null) {
            throw new RuntimeException("Error al obtener las solicitudes de eliminacion en el servicio externo");
        }

        return response;
    }

    public PageSolicitudCambioDTO obtenerTodasLasSolicitudesCambio(int page, int limit, String estado) {

        int pageBackend = page;
        StringBuilder url = new StringBuilder(
                dinamicApi + "/priv/hechos/solicitudes?page=" + pageBackend + "&limit=" + limit
        );

        if (estado != null && !estado.trim().isEmpty()) {

            Boolean estadoBoolean;

            switch (estado.toUpperCase()) {
                case "RESUELTA":
                    estadoBoolean = true;
                    break;

                case "PENDIENTE":
                    estadoBoolean = false;
                    break;

                default:
                    estadoBoolean = null;
            }

            if (estadoBoolean != null) {
                // 👀 OJO: acá tiene que ir EXACTAMENTE el nombre del parámetro
                // que espera la API dinámica. Si allá es "estado", cambiá "resuelta"
                url.append("&resuelta=").append(estadoBoolean);
            }
        }

        String finalUrl = url.toString();


        PageSolicitudCambioDTO response =
                webApiCallerService.getAdmin(finalUrl, PageSolicitudCambioDTO.class);

        if (response == null) {
            throw new RuntimeException("Error al obtener las solicitudes de cambio en el servicio externo");
        }

        // ver qué viene realmente


        return response;
    }

    public HechoDTO crearHecho(HechoInputDTO hecho) {
        System.out.println(hecho);
        System.out.println("print pre llamada ");

        String url = dinamicApi + "/hechos/new";

        try {
            // Logueo del URL y body
            String bodyJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(hecho);
            System.out.println("Llamando a webApiCallerService.post -> URL: " + url + " BODY: " + bodyJson);

            HechoDTO response = webApiCallerService.post(url, hecho, HechoDTO.class);
            System.out.println("Respuesta webApiCallerService: " + response);

            if (response == null) {
                throw new RuntimeException("Error al crear hecho en el servicio externo (respuesta nula)");
            }
            return response;
        } catch (WebClientResponseException webEx) {
            // Log detallado de la respuesta 4xx/5xx del backend
            System.out.println(">>> WebClientResponseException status: " + webEx.getRawStatusCode());
            System.out.println(">>> WebClientResponseException body: " + webEx.getResponseBodyAsString());
            log.error("Error al crear hecho (status {}): {}", webEx.getRawStatusCode(), webEx.getResponseBodyAsString(), webEx);
            throw new RuntimeException("Bad request al crear hecho: " + webEx.getResponseBodyAsString(), webEx);
        } catch (Exception e) {
            log.error("Error al crear hecho via webApiCallerService: {}", e.getMessage(), e);
            System.out.println("Error en webApiCallerService: " + e.getMessage());
            // Intento de fallback directo con WebClient para diagnosticar
            try {
                System.out.println("Intentando llamada directa con WebClient a: " + url);
                HechoDTO direct = webClient.post()
                        .uri(url)
                        .bodyValue(hecho)
                        .retrieve()
                        .bodyToMono(HechoDTO.class)
                        .block();
                System.out.println("Respuesta directa WebClient: " + direct);
                if (direct == null) {
                    throw new RuntimeException("Respuesta directa nula");
                }
                return direct;
            } catch (WebClientResponseException webEx2) {
                System.out.println(">>> Fallback WebClient status: " + webEx2.getRawStatusCode());
                System.out.println(">>> Fallback WebClient body: " + webEx2.getResponseBodyAsString());
                log.error("Fallback WebClient response body: {}", webEx2.getResponseBodyAsString(), webEx2);
                throw new RuntimeException("Bad request en fallback: " + webEx2.getResponseBodyAsString(), webEx2);
            } catch (Exception ex) {
                log.error("Error en llamada directa WebClient: {}", ex.getMessage(), ex);
                throw new RuntimeException("No se pudo crear hecho. webApiCallerService error: " + e.getMessage() + " / fallback error: " + ex.getMessage(), ex);
            }
        }
    }
// Archivo: MetaMapaApiService.java

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


    public void actualizarEstadoSolicitud(Long idSolicitud, String nuevoEstado) {

        // 1. Traducir el Estado (String) al Char que pide tu Backend ('A', 'R', 'S')
        char codigoEstado;

        if ("APROBADA".equalsIgnoreCase(nuevoEstado)) {
            codigoEstado = 'A';
        } else if ("RECHAZADA".equalsIgnoreCase(nuevoEstado)) {
            codigoEstado = 'R';
        } else if ("SPAM".equalsIgnoreCase(nuevoEstado)) {
            codigoEstado = 'S';
        } else {
            // Default por seguridad
            codigoEstado = 'R';
        }


        //  Usamos 'agregacionApi' + la ruta base.

        String url = agregacionApi + "/priv/solicitudes/" + idSolicitud + "/" + codigoEstado;

        System.out.println(">>> Llamando a Backend (PUT): " + url);

        // 3. Ejecutar llamada SIN BODY (null), porque el dato ya va en la URL
        try {
            webApiCallerService.putAdmin(url, Map.of(), Void.class);
        } catch (Exception e) {
            log.error("Error al actualizar solicitud {}: {}", idSolicitud, e.getMessage());
            throw new RuntimeException("Error en backend: " + e.getMessage());
        }
    }

    public void actualizarEstadoSolicitudCambio(Long idSolicitud, Long idAdmin, boolean aceptada) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(dinamicApi)
                    .pathSegment("priv", "hechos", "solicitudCambio", String.valueOf(idSolicitud))
                    .queryParam("idAdmin", idAdmin)
                    .queryParam("aceptada", aceptada)
                    .toUriString();

            System.out.println(">>> PUT actualizarEstadoSolicitudCambio -> " + url);

            // No mando body, solo query params, y no me interesa el DTO de vuelta
            webApiCallerService.putAdmin(url,Map.of(), Void.class);

        } catch (WebClientResponseException e) {
            System.out.println(">>> WebClientResponseException status: " + e.getRawStatusCode());
            System.out.println(">>> WebClientResponseException body: " + e.getResponseBodyAsString());
            log.error("Error al actualizar solicitud de cambio (status {}): {}",
                    e.getRawStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Error en backend: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Error al actualizar solicitud de cambio {}: {}", idSolicitud, e.getMessage(), e);
            throw new RuntimeException("Error en backend: " + e.getMessage(), e);
        }
    }

}
