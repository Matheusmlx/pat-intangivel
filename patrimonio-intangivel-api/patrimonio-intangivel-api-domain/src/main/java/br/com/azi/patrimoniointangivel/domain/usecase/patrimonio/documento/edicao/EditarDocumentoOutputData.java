package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarDocumentoOutputData {
    private Long id;
    private String numero;
    private LocalDateTime data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long idTipoDocumento;
    private Long idPatrimonio;
}
