package br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarorgao;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.unidadeorganizacional.buscarorgao.converter.BuscarUnidadesOrganizacionaisOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BuscarUnidadesOrganizacionaisUseCaseImpl implements BuscarUnidadesOrganizacionaisUseCase {

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarUnidadesOrganizacionaisOutputDataConverter outputDataConverter;

    @Override
    public BuscarUnidadesOrganizacionaisOutputData executar() {
        List<UnidadeOrganizacional> entidadesEncontradas = buscar();

        return outputDataConverter.to(entidadesEncontradas);
    }

    private List<UnidadeOrganizacional> buscar() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoPatrimonioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoPatrimonioConstants.CONSULTA.getDescription());
        return sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);
    }
}


