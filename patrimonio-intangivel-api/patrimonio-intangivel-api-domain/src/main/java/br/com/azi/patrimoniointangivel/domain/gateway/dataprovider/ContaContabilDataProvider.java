package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;


import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;

import java.util.Optional;

public interface ContaContabilDataProvider {

    Optional<ContaContabil> buscarPorId(Long contaContabilId);

    Optional<ContaContabil> buscarPorCodigo(String codigo);

    ListaPaginada<ContaContabil> buscarPorFiltro(ContaContabil.Filtro filtro);

}
