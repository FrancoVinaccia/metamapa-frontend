package ar.utn.ba.ddsi.metaMapa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EPageOutputDTO {
    private Integer page;
    private Integer limit;
    private Integer total;
    private List<Object> items;
}
