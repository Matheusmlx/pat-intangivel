package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.NaoPossuiAcessoAoOrgaoVinculadoException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarporid.converter.BuscarPatrimonioPorIdOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception.EditarPatrimonioAtivoException;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarPatrimonioPorIdUseCaseImpl implements BuscarPatrimonioPorIdUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarPatrimonioPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarPatrimonioPorIdOutputData executar(BuscarPatrimonioPorIdInputData inputData) {
        validarDadosEntrada(inputData);
        Patrimonio entidadeEncontrada = buscar(inputData);
        verificarPatrimonioAtivo(entidadeEncontrada);
        verificarAcessoAoOrgao(entidadeEncontrada);
        return this.outputDataConverter.to(entidadeEncontrada);
    }

    private void validarDadosEntrada(BuscarPatrimonioPorIdInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarPatrimonioPorIdInputData::getId, Objects::nonNull, "O id é nulo")
            .get();
    }

    private Patrimonio buscar(BuscarPatrimonioPorIdInputData inputData) {
        Optional<Patrimonio> entidade = patrimonioDataProvider.buscarPorId(inputData.getId());
        return entidade.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private void verificarPatrimonioAtivo(Patrimonio patrimonio) {
        if (patrimonio.getSituacao().equals(Patrimonio.Situacao.ATIVO)) {
            throw new EditarPatrimonioAtivoException();
        }
    }

    private void verificarAcessoAoOrgao(Patrimonio patrimonio) {
        if (Objects.nonNull(patrimonio.getOrgao())) {
            Boolean possuiAcessoAoOrgao = sistemaDeGestaoAdministrativaIntegration.verificarDominioUnidadeOrganizacionalPorIdEFuncao(
                patrimonio.getOrgao().getId(),
                Collections.singletonList(PermissaoPatrimonioConstants.CONSULTA.getDescription())
            );

            if (!possuiAcessoAoOrgao) {
                throw new NaoPossuiAcessoAoOrgaoVinculadoException();
            }
        }
    }

}
