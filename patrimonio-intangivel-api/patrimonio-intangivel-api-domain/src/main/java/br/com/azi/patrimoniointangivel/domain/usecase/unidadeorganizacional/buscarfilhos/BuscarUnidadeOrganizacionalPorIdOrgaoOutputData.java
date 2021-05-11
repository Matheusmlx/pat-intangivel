package br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarfilhos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarUnidadeOrganizacionalPorIdOrgaoOutputData {

    List<UnidadeOrganizacional> items;

    @Data
    @NoArgsConstructor
    public static class UnidadeOrganizacional {
        private Long id;
        private String sigla;
        private String nome;
        private String codHierarquia;
        private String descricao;
    }
}
