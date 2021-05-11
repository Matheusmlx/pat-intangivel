package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarDocumentoMovimentacaoIntputData {
    private Long movimentacaoId;
}
