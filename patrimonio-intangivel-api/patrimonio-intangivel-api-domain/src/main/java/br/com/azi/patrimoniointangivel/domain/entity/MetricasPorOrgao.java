package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricasPorOrgao {
    private Long idOrgao;
    private String sigla;
    private String nome;
    private Long total;
    private List<Totalizador> tipos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Totalizador {
        private String nome;
        private Long quantidade;
    }
}
