package br.com.azi.patrimoniointangivel.domain.gateway.integration;

import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;

public interface SistemaDeFornecedoresIntegration {

    ListaPaginada<Fornecedor> buscarPorFiltro(Fornecedor.Filtro filtro);

    Fornecedor buscarPorId(Long id);

}
