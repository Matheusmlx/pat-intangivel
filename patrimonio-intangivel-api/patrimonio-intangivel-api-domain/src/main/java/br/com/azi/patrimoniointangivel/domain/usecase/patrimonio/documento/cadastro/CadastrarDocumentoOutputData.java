package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarDocumentoOutputData {
    private Long id;
    private String numero;
    private String data;
    private BigDecimal valor;
    private String uriAnexo;
    private Long idTipoDocumento;
    private Long idPatrimonio;
}
