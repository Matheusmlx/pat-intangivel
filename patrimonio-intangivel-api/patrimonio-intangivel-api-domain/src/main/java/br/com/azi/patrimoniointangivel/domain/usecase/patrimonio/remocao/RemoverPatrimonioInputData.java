package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoverPatrimonioInputData {
    private Long id;
}
