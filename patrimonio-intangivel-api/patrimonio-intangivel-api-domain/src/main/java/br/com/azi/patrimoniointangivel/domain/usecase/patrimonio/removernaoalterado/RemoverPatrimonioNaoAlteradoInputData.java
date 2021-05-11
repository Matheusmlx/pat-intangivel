package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.removernaoalterado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoverPatrimonioNaoAlteradoInputData {
    private Long id;
}
