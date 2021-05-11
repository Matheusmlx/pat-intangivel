package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioListagemPatrimonios {
    private List<ContaContabil> contasContabeis;
    private LocalDateTime dataRelatorio;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patrimonio {
        private Long id;
        private String numero;
        private String nome;
        private String tipo;
        private Boolean amortizavel;
        private BigDecimal valorLiquido;
        private BigDecimal valorAquisicao;
        private BigDecimal valorAmortizadoMensal;
        private String situacaoPatrimonio;
        private String descricao;
        private String orgao;
        private String setor;
        private String reconhecimento;
        private String estado;
        private BigDecimal valorAmortizadoAcumulado;
        private LocalDateTime dataAquisicao;
        private LocalDateTime dataAtivacao;
        private String fornecedor;
        private BigDecimal valorEntrada;
        private LocalDateTime vencimentoLicenca;
        private String contaContabil;
        private DadosAmortizacao taxaAmortizada;
        private BigDecimal amortizacaoAcumulada;
        private NotaLancamentoContabil notaLancamentoContabil;
    }
}
