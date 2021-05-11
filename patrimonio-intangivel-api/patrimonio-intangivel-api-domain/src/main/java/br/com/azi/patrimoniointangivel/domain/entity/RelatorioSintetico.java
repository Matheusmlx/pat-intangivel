package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioSintetico {

    private UnidadeOrganizacional orgao;
    private Long patrimonioId;
    private BigDecimal valorLiquido;
    private BigDecimal valorAquisicao;
    private LocalDateTime dataAtivacao;
    private LocalDateTime dataCadastro;
    private ContaContabil contaContabil;
    private BigDecimal valorAmortizadoMensal;
    private BigDecimal valorAmortizadoAcumulado;
    private UnidadeOrganizacional orgaoAmortizacao;
}
