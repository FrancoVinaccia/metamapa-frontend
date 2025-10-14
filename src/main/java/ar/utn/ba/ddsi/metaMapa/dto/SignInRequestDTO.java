package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // Lombok para generar getters, setters, toString(), etc.
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class SignInRequestDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String fechaNacimiento;
}