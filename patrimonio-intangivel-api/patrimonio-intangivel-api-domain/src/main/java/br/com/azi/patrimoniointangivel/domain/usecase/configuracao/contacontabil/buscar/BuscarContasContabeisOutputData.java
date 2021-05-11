package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarContasContabeisOutputData {

    List<Item> items;
    Long totalPages;
    Long totalElements;

    @Data
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String codigo;
        private String nome;
        private String tipo;
        private String metodo;
        private Long idConfigAmortizacao;
    }
}
