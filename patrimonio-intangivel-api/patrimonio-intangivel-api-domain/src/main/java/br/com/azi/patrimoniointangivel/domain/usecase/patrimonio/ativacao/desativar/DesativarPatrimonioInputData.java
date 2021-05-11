package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DesativarPatrimonioInputData {

    private Long id;
}
