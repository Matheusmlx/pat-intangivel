package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarDocumentoInputData {
    private Long patrimonioId;
}
