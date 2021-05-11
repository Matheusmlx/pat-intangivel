package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarPatrimonioPorMovimentacaoInputData {
    private Long id;
}
