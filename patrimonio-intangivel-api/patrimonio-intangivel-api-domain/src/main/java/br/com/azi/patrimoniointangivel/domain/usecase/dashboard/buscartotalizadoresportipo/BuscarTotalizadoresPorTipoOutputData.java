package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo;

import br.com.azi.patrimoniointangivel.domain.entity.PatrimoniosPorTipo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarTotalizadoresPorTipoOutputData {
    List<PatrimoniosPorTipo> itens;

}
