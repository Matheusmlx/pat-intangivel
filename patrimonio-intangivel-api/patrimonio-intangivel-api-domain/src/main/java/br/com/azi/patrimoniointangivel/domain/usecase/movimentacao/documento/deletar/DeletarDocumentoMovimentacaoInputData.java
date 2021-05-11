package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletarDocumentoMovimentacaoInputData {
    private Long id;
    private Long movimentacaoId;
}
