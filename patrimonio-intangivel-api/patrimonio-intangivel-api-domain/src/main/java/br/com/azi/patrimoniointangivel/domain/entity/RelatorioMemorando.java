package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioMemorando {
    private String numeroAno;
    private String mesAno;
    private Patrimonio patrimonio;
    private ContaContabil contaContabil;
    private Fornecedor fornecedor;
    private Reconhecimento reconhecimento;
    private Orgao orgao;
    private Setor setor;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patrimonio {
        private String numero;
        private String nome;
        private String dataNL;
        private String numeroNL;
        private String descricao;
        private BigDecimal valorAquisicao;
        private BigDecimal valorEntrada;
        private String dataAtivacao;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContaContabil {
        private String nome;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Fornecedor {
        private String nome;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reconhecimento {
        private String nome;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Orgao {
        private String nome;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Setor {
        private String nome;
    }
}
