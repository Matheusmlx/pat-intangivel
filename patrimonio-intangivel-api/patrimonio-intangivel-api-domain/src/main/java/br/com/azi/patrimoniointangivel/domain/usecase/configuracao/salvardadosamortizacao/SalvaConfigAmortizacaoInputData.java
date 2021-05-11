package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalvaConfigAmortizacaoInputData {

    private Long contaContabil;
    private String tipo;
    private Short vidaUtil;
}
