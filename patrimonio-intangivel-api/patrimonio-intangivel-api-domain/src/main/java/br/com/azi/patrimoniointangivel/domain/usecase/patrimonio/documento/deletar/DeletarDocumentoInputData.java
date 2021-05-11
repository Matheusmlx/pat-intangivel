package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletarDocumentoInputData {
    private Long id;
    private Long patrimonioId;
}

