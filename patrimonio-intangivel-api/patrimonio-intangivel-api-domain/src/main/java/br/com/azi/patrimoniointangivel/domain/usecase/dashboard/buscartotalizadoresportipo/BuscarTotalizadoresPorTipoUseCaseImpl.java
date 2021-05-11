package br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio.Tipo;
import br.com.azi.patrimoniointangivel.domain.entity.PatrimoniosPorTipo;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.dashboard.buscartotalizadoresportipo.converter.BuscarTotalizadoresPorTipoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarTotalizadoresPorTipoUseCaseImpl implements BuscarTotalizadoresPorTipoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarTotalizadoresPorTipoOutputDataConverter outputDataConverter;

    @Override
    public BuscarTotalizadoresPorTipoOutputData executar() {
        List<PatrimoniosPorTipo> patrimoniosPorTipos = new ArrayList<>();
        List<Long> idsOrgaosDoUsuario = buscarOrgaosIdsAcessoUsuario();

        buscarTotalizadorPorTipoNoOrgao(patrimoniosPorTipos, Tipo.SOFTWARES, idsOrgaosDoUsuario);
        buscarTotalizadorPorTipoNoOrgao(patrimoniosPorTipos, Tipo.DIREITOS_AUTORAIS, idsOrgaosDoUsuario);
        buscarTotalizadorPorTipoNoOrgao(patrimoniosPorTipos, Tipo.LICENCAS, idsOrgaosDoUsuario);
        buscarTotalizadorPorTipoNoOrgao(patrimoniosPorTipos, Tipo.MARCAS, idsOrgaosDoUsuario);
        buscarTotalizadorPorTipoNoOrgao(patrimoniosPorTipos, Tipo.TITULOS_DE_PUBLICACAO, idsOrgaosDoUsuario);
        buscarTotalizadorPorTipoNoOrgao(patrimoniosPorTipos, Tipo.RECEITAS_FORMULAS_PROJETOS, idsOrgaosDoUsuario);

        return outputDataConverter.to(patrimoniosPorTipos);
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

    private void buscarTotalizadorPorTipoNoOrgao(List<PatrimoniosPorTipo> patrimoniosPorTipos, Tipo tipo, List<Long> idsOrgaosDoUsuario) {
        patrimoniosPorTipos.add(
            PatrimoniosPorTipo.builder()
                .nome(tipo.getValor())
                .tipo(tipo.toString())
                .quantidade(contarPorTipoEOrgaos(tipo, idsOrgaosDoUsuario))
                .build()
        );
    }

    private Long contarPorTipoEOrgaos(Tipo tipo, List<Long> idsOrgaosDoUsuario) {
        return patrimonioDataProvider.contarPorTipoEOrgaos(tipo.toString(), idsOrgaosDoUsuario);
    }
}
