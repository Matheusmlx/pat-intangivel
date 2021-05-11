package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarContaContabilPorIdInputData {
    private Long id;
    private Long produtoId;
}
