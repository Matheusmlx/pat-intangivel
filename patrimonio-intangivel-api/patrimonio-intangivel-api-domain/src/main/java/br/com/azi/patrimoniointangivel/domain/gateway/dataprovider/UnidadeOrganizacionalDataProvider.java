package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;

import java.util.Optional;

public interface UnidadeOrganizacionalDataProvider {

    Optional<UnidadeOrganizacional> buscarPorId(Long id);
}
