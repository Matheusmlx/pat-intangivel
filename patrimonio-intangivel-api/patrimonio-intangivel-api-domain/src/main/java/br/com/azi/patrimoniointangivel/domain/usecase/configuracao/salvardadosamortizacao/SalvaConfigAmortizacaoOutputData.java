package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalvaConfigAmortizacaoOutputData {

    private Long id;
    private String sistema;
    private Long contaContabil;
    private String metodo;
    private Short vidaUtil;
    private String situacao;
    private BigDecimal taxa;
    private BigDecimal percentualResidual;
    private String tipo;
}
