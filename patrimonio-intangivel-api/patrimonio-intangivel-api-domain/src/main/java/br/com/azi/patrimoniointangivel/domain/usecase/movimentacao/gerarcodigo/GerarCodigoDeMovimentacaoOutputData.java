package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GerarCodigoDeMovimentacaoOutputData {
    private String codigo;
}
