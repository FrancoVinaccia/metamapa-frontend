package ar.utn.ba.ddsi.metaMapa.services;

import ar.utn.ba.ddsi.metaMapa.models.dto.HechoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechoService {

    //TODO: logica servcice

    public List<HechoDTO> obtenerTodosLosHechos() {
        // Lógica para obtener todos los hechos del repositorio
        return List.of();
    }

    public List<HechoDTO> hechosDestacados() {
        HechoDTO h1 = new HechoDTO(); h1.setId(1); h1.setTitulo("Hecho mock 1");
        h1.setDescripcion("Descripción mock 1");

        HechoDTO h2 = new HechoDTO(); h2.setId(2); h2.setTitulo("Hecho mock 2");
        h2.setDescripcion("Descripción mock 2");

        return List.of(h1, h2);
    }

    public HechoDTO visualizarHecho(Long id) {
        // Lógica para obtener un hecho por su ID
        return new HechoDTO();
    }
}
