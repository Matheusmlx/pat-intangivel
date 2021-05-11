package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;


import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;

import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioSinteticoDataProvider {


    List<RelatorioSintetico> buscaRelatorioSintetico(Long orgaoId, LocalDateTime dataFinal);
}
