package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String nombreUsuario;
    private String email;
    private Date fechaNacimiento;
    private Rol rol;
    private String token;
    private String refreshToken;
}