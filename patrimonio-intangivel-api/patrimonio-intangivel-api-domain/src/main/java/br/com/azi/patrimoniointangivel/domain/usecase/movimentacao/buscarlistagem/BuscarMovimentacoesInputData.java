package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarMovimentacoesInputData {
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
    private String conteudoExtra;
}
