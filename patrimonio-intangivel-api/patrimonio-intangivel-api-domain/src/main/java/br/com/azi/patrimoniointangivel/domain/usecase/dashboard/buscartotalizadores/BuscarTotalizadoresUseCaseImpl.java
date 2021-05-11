package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadores.converter.BuscarTotalizadoresOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarTotalizadoresUseCaseImpl implements BuscarTotalizadoresUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarTotalizadoresOutputDataConverter buscarTotalizadoresOutputDataConverter;

    @Override
    public BuscarTotalizadoresOutputData executar() {
        List<Long> idsOrgaosDoUsuario = buscarOrgaosIdsAcessoUsuario();

        return buscarTotalizadoresOutputDataConverter.to(
            patrimonioDataProvider.contarTotalDeRegistrosPorOrgaos(idsOrgaosDoUsuario),
            patrimonioDataProvider.contarEmElaboracaoPorOrgaos(idsOrgaosDoUsuario),
            patrimonioDataProvider.contarAtivosPorOrgaos(idsOrgaosDoUsuario)
        );
    }

    private List<Long> buscarOrgaosIdsAcessoUsuario() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoPatrimonioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoPatrimonioConstants.CONSULTA.getDescription());

        List<UnidadeOrganizacional> unidadeOrganizacionals = sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);

        return unidadeOrganizacionals
            .stream()
            .map(UnidadeOrganizacional::getId)
            .distinct()
            .collect(Collectors.toList());
    }

}
