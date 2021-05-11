package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.RelatorioListagemPatrimonios;

import java.util.List;

public interface RelatorioListagemPatrimoniosDataProvider {

    List<RelatorioListagemPatrimonios> buscarRelatorioListagem();
}
