package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditarDocumentoMovimentacaoInputData {
    private Long id;
    private String numero;
    private Date data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long idMovimentacao;
    private Long idTipoDocumento;
}
