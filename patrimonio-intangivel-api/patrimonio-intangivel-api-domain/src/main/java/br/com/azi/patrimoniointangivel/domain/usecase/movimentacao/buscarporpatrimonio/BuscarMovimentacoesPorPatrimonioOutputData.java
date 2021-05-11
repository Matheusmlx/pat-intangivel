package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporpatrimonio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarMovimentacoesPorPatrimonioOutputData {

    List<Item> items;

    @Data
    @NoArgsConstructor
    public static class Item{
        private Long id;
        private String motivo;
        private UnidadeOrganizacional orgaoOrigem;
        private UnidadeOrganizacional orgaoDestino;
        private String situacao;
        private String tipo;
        private LocalDateTime dataDeAlteracao;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnidadeOrganizacional{
        private Long id;
        private String descricao;
        private String nome;
        private String sigla;
    }
}
