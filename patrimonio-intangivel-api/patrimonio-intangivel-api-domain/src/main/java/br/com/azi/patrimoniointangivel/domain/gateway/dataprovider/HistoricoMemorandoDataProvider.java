package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.HistoricoMemorando;

public interface HistoricoMemorandoDataProvider {

    HistoricoMemorando salvar(HistoricoMemorando memorando);
}
