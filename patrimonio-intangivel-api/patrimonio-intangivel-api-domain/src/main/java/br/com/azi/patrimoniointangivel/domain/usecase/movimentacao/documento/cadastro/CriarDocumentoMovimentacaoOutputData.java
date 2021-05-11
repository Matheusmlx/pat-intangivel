package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriarDocumentoMovimentacaoOutputData {
    private Long id;
    private String numero;
    private String data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long idTipoDocumento;
    private Long idMovimentacao;
}
