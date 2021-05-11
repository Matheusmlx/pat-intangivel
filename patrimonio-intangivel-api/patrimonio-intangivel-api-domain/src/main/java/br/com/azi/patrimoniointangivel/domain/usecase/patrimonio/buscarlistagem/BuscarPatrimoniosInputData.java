package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarPatrimoniosInputData {
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
}
