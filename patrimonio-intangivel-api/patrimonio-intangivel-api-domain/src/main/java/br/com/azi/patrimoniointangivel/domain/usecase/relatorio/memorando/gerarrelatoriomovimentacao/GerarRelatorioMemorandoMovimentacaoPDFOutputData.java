package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarRelatorioMemorandoMovimentacaoPDFOutputData {
    private String nome;
    private String contentType;
    private byte[] content;
    private String uri;
    private String url;
}
