package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.NaoPossuiAcessoAoOrgaoVinculadoException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.converter.VisualizarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class VisualizarPatrimonioUseCaseImpl implements VisualizarPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private AmortizacaoDataProvider amortizacaoDataProvider;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private VisualizarPatrimonioOutputDataConverter outputDataConverter;

    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Override
    public VisualizarPatrimonioOutputData executar(VisualizarPatrimonioInputData inputData) {
        validarDadosEntrada(inputData);
        Patrimonio entidadeEncontrada = buscar(inputData);
        verificarAcessoAoOrgao(entidadeEncontrada);

        verificarPossibilidadeDesativacao(entidadeEncontrada);

        return outputDataConverter.to(entidadeEncontrada);
    }

    private void validarDadosEntrada(VisualizarPatrimonioInputData inputData) {
        Validator.of(inputData)
            .validate(VisualizarPatrimonioInputData::getId, Objects::nonNull, "O id é nulo")
            .get();
    }

    private Patrimonio buscar(VisualizarPatrimonioInputData inputData) {
        Optional<Patrimonio> entidade = patrimonioDataProvider.buscarPorId(inputData.getId());
        return entidade.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private void verificarAcessoAoOrgao(Patrimonio patrimonio) {
        if (Objects.nonNull(patrimonio.getOrgao())) {
            Boolean possuiAcessoAoOrgao = sistemaDeGestaoAdministrativaIntegration.verificarDominioUnidadeOrganizacionalPorIdEFuncao(
                patrimonio.getOrgao().getId(),
                Arrays.asList(PermissaoPatrimonioConstants.NORMAL.getDescription(), PermissaoPatrimonioConstants.CONSULTA.getDescription())
            );

            if (!possuiAcessoAoOrgao) {
                throw new NaoPossuiAcessoAoOrgaoVinculadoException();
            }
        }
    }

    private void verificarPossibilidadeDesativacao(Patrimonio patrimonio) {
        Boolean existeMovimentacoes = verificarSeExisteMovimentacoes(patrimonio);

        boolean ativadoEsteMes = DateUtils.mesmoMesAno(patrimonio.getDataFinalAtivacao(), LocalDateTime.now());
        patrimonio.setPermitirDesativacao(ativadoEsteMes && !validarSeExisteAmortizacaoPorPatrimonio(patrimonio) && !existeMovimentacoes);
    }

    private Boolean validarSeExisteAmortizacaoPorPatrimonio(Patrimonio patrimonio) {
        return amortizacaoDataProvider.existePorPatrimonio(patrimonio.getId());
    }

    private Boolean verificarSeExisteMovimentacoes(Patrimonio patrimonio){
        return movimentacaoDataProvider.existePorIdPatrimonio(patrimonio.getId());
    }
}
