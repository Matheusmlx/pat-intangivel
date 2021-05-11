package br.com.azi.patrimoniointangivel.gateway.integration.hal;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.buscarunidadeorganozacionalporid.BuscarUnidadeOrganizacionalPorIdIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.buscarunidadesorganizacionais.BuscarUnidadesOrganizacionaisIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.buscarunidadesorganizacionaisporfuncao.BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.hal.usecase.unidadeorganizacional.verificardominiounidadeorganizacionalporidefuncao.VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class SistemaDeGestaoAdministrativaIntegrationHalImpl implements SistemaDeGestaoAdministrativaIntegration {

    private HalIntegrationProperties integrationProperties;

    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private BuscarUnidadesOrganizacionaisIntegrationUseCase buscarUnidadesOrganizacionaisIntegrationUseCase;

    private BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase buscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase;

    private BuscarUnidadeOrganizacionalPorIdIntegrationUseCase buscarUnidadeOrganizacionalPorIdIntegrationUseCase;

    private VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase verificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase;

    @Override
    public List<UnidadeOrganizacional> buscarUnidadesOrganizacionais() {
        return buscarUnidadesOrganizacionaisIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get());
    }

    @Override
    public List<UnidadeOrganizacional> buscarUnidadesOrganizacionaisPorFuncao(List<String> funcoes) {
        return buscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), funcoes);
    }

    @Override
    public List<UnidadeOrganizacional> buscarUnidadeOrganizacionalPorId(Long orgaoId) {
        return buscarUnidadeOrganizacionalPorIdIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), orgaoId);
    }

    @Override
    public Boolean verificarDominioUnidadeOrganizacionalPorIdEFuncao(Long orgaoId, List<String> funcoes) {
        return verificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), orgaoId, funcoes);
    }

}
