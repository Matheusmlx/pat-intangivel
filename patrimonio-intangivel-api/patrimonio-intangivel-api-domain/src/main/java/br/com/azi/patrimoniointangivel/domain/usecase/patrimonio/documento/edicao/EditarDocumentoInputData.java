package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarDocumentoInputData {
    private Long id;
    private String numero;
    private Date data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long idPatrimonio;
    private Long idTipoDocumento;
}
