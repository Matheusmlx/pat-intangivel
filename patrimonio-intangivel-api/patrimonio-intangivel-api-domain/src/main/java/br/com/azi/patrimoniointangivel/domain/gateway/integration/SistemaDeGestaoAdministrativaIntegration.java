package br.com.azi.patrimoniointangivel.domain.gateway.integration;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;

import java.util.List;

public interface SistemaDeGestaoAdministrativaIntegration {

    List<UnidadeOrganizacional> buscarUnidadesOrganizacionais();

    List<UnidadeOrganizacional> buscarUnidadesOrganizacionaisPorFuncao(List<String> funcoes);

    List<UnidadeOrganizacional> buscarUnidadeOrganizacionalPorId(Long orgaoId);

    Boolean verificarDominioUnidadeOrganizacionalPorIdEFuncao(Long orgaoId, List<String> funcoes);

}
