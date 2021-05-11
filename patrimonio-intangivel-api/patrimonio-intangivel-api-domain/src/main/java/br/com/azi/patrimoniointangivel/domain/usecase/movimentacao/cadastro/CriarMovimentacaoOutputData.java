package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CriarMovimentacaoOutputData {
    private Long id;
    private String codigo;
    private String situacao;
    private String tipo;
    private Long orgaoOrigem;
}
