package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtivarPatrimonioOutputData {
    private Long id;
    private String numero;
}
