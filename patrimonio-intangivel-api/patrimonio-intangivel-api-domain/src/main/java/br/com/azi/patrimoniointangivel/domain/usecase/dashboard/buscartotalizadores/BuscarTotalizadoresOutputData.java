package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarTotalizadoresOutputData {

    Long totalDeRegistros;
    Long emElaboracao;
    Long ativos;
}
