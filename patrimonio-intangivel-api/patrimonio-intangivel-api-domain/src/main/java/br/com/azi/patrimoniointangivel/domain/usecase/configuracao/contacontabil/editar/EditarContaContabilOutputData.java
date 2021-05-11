package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarContaContabilOutputData {

    private Long id;
    private Long contaContabil;
    private String metodo;
    private String tipo;
}
