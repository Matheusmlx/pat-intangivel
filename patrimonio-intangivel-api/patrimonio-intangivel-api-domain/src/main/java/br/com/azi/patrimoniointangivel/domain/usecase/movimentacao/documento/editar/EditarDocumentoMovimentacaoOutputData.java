package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditarDocumentoMovimentacaoOutputData {
    private Long id;
    private String numero;
    private LocalDateTime data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long idTipoDocumento;
    private Long idMovimentacao;
}
