package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro;

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
public class CadastrarDocumentoInputData {
    private String numero;
    private BigDecimal valor;
    private Date data;
    private String uriAnexo;
    private Long idTipoDocumento;
    private Long idPatrimonio;
}
