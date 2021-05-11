package br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarUnidadeOrganizacionalPorIdOrgaoInputData {
    private Long id;
}
