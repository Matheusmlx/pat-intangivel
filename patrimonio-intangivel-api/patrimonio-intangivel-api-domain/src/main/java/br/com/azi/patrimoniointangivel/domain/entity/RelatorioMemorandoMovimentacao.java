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
public class RelatorioMemorandoMovimentacao {
    private String numeroAno;
    private String mesAno;
    private Movimentacao movimentacao;
    private String contaContabil;
    private Patrimonio patrimonio;
    private String orgaoOrigem;
    private String orgaoDestino;
    private String mensagemRodapeMemorandoMovimentacao;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Movimentacao{
        private String codigo;
        private String tipo;
        private String dataEnvio;
        private String dataRecebido;
        private String motivo;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patrimonio{
        private String numero;
        private String nome;
        private String tipo;
        private BigDecimal valorLiquido;
        private BigDecimal valorAquisicao;
        private BigDecimal valorEntrada;
        private BigDecimal valorAmortizadoAcumulado;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Orgao {
        private String nome;
        private String sigla;
    }

}
