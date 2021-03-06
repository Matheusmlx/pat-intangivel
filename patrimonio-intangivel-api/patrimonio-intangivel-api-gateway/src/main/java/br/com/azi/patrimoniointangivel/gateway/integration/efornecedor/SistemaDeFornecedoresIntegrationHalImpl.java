package br.com.azi.patrimoniointangivel.gateway.integration.efornecedor;

import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.entity.EfornecedorIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporfiltro.BuscarFornecedoresPorFiltroIntegrationUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporid.BuscarFornecedorPorIdIntegrationUseCase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SistemaDeFornecedoresIntegrationHalImpl implements SistemaDeFornecedoresIntegration {

    private EfornecedorIntegrationProperties integrationProperties;

    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private BuscarFornecedoresPorFiltroIntegrationUseCase buscarFornecedoresPorFiltroIntegrationUseCase;

    private BuscarFornecedorPorIdIntegrationUseCase buscarFornecedorPorIdIntegrationUseCase;

    @Override
    public ListaPaginada<Fornecedor> buscarPorFiltro(Fornecedor.Filtro filtro) {
        return buscarFornecedoresPorFiltroIntegrationUseCase.executar(filtro, sessaoUsuarioDataProvider.get(), integrationProperties);
    }

    @Override
    public Fornecedor buscarPorId(Long id) {
        return buscarFornecedorPorIdIntegrationUseCase.executar(id, sessaoUsuarioDataProvider.get(), integrationProperties);
    }
}
