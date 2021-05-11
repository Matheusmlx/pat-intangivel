package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarMovimentacoesOutputData {
    List<Item> items;
    Long totalPages;
    Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item{
        private Long id;
        private Long patrimonio;
        private String codigo;
        private String dataCadastro;
        private String tipo;
        private String orgaoOrigem;
        private String orgaoDestino;
        private String situacao;
    }
}
