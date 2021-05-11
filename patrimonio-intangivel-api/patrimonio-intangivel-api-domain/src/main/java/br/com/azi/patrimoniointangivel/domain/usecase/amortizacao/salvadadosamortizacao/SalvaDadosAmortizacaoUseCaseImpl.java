package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.exception.AmortizacaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.exception.QuotasContantesValidacaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class SalvaDadosAmortizacaoUseCaseImpl implements SalvaDadosAmortizacaoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    private AmortizacaoDataProvider amortizacaoDataProvider;

    private QuotasConstantesUseCase quotasConstantesUseCase;

    @Override
    public void executar(SalvaDadosAmortizacaoInputData inputData) {
        validarDadosEntrada(inputData);

        Patrimonio patrimonio = buscar(inputData);
        validarExistemDadosdeAmortizacao(patrimonio);

        DadosAmortizacao dadosAmortizacao = buscarVinculoAmortizacao(patrimonio);
        verificarExisteConfiguracaoAmortizacao(dadosAmortizacao);

        ConfigAmortizacao configAmortizacao = buscar(dadosAmortizacao);
        verificarSeConfiguracaoAmortizacaoAtiva(configAmortizacao);

        Boolean ultimoMesVidaUtil = verificarUltimoMesVidaUtil(patrimonio, inputData);
        QuotasConstantesOutputData quotasConstantesOutputData = calcularQuotasContantes(inputData, configAmortizacao, patrimonio, ultimoMesVidaUtil);

        if (ultimoMesVidaUtil){
            inputData.setDataFinal(patrimonio.getFimVidaUtil());
        }

        Amortizacao amortizacao = criarEntidade(quotasConstantesOutputData, inputData, patrimonio, configAmortizacao);
        salvar(amortizacao);

        atualizarValorLiquidoPatrimonio(patrimonio, quotasConstantesOutputData);

    }

    private void validarDadosEntrada(SalvaDadosAmortizacaoInputData inputData) {
        Validator.of(inputData)
            .validate(SalvaDadosAmortizacaoInputData::getPatrimonio, Objects::nonNull, "Id do patrimônio é nulo!")
            .validate(SalvaDadosAmortizacaoInputData::getDataInicio, Objects::nonNull, "Data incial é nula!")
            .validate(SalvaDadosAmortizacaoInputData::getDataFinal, Objects::nonNull, "Data final é nula!")
            .get();
    }

    private Patrimonio buscar(SalvaDadosAmortizacaoInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getPatrimonio());
        return patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private void validarExistemDadosdeAmortizacao(Patrimonio patrimonio) {
        if (!dadosAmortizacaoDataProvider.existe(patrimonio.getDadosAmortizacao().getId())) {
            throw new QuotasContantesValidacaoException("Vínculo de dados de amortização não encontrado!");
        }
    }

    private DadosAmortizacao buscarVinculoAmortizacao(Patrimonio patrimonio) {
        Optional<DadosAmortizacao> dadosAmortizacaoEncontrados = dadosAmortizacaoDataProvider.buscarPorId(patrimonio.getDadosAmortizacao().getId());
        return dadosAmortizacaoEncontrados.orElseThrow(AmortizacaoException::new);
    }

    private void verificarExisteConfiguracaoAmortizacao(DadosAmortizacao dadosAmortizacao) {
        if (Objects.nonNull(dadosAmortizacao.getConfigAmortizacao())) {
            if (!configAmortizacaoDataProvider.existe(dadosAmortizacao.getConfigAmortizacao().getId())) {
                throw new QuotasContantesValidacaoException("Configuração de amortização não encontrada!");
            }
        } else {
            throw new QuotasContantesValidacaoException("Configuração de Amortização é nula!");
        }
    }

    private ConfigAmortizacao buscar(DadosAmortizacao dadosAmortizacao) {
        Optional<ConfigAmortizacao> configAmortizacao = configAmortizacaoDataProvider.buscarPorId(dadosAmortizacao.getConfigAmortizacao().getId());
        return configAmortizacao.orElseThrow(AmortizacaoException::new);
    }

    private void verificarSeConfiguracaoAmortizacaoAtiva(ConfigAmortizacao configAmortizacao) {
        if (!configAmortizacao.getSituacao().equals(ConfigAmortizacao.Situacao.ATIVO)) {
            throw new QuotasContantesValidacaoException("Configuração de amortização não está ativa!");
        }
    }

    private Boolean verificarUltimoMesVidaUtil(Patrimonio patrimonio, SalvaDadosAmortizacaoInputData inputData) {
        return (patrimonio.getFimVidaUtil().getMonth() == inputData.getDataFinal().getMonth()
            && patrimonio.getFimVidaUtil().getYear() == inputData.getDataFinal().getYear());
    }

    private QuotasConstantesOutputData calcularQuotasContantes(SalvaDadosAmortizacaoInputData inputData, ConfigAmortizacao configAmortizacao,
                                                               Patrimonio patrimonio, Boolean ultimoMesVidaUtil) {
        QuotasConstantesInputData input = QuotasConstantesInputData
            .builder()
            .dataFinal(inputData.getDataFinal())
            .dataInicio(inputData.getDataInicio())
            .percentualResidual(configAmortizacao.getPercentualResidual())
            .taxa(configAmortizacao.getTaxa())
            .valorAquisicao(patrimonio.getValorAquisicao())
            .valorLiquido(patrimonio.getValorLiquido())
            .ultimoMesVidaUtil(ultimoMesVidaUtil)
            .build();

        return quotasConstantesUseCase.executar(input);
    }

    private Amortizacao criarEntidade(QuotasConstantesOutputData quotasConstantes, SalvaDadosAmortizacaoInputData inputData,
                                      Patrimonio patrimonio, ConfigAmortizacao configAmortizacao) {
        return Amortizacao
            .builder()
            .valorAnterior(quotasConstantes.getValorAnterior())
            .valorPosterior(quotasConstantes.getValorPosteiror())
            .valorSubtraido(quotasConstantes.getValorSubtraido())
            .taxaAplicada(quotasConstantes.getTaxaAplicada())
            .dataInicial(inputData.getDataInicio())
            .dataFinal(inputData.getDataFinal())
            .patrimonio(patrimonio)
            .orgao(patrimonio.getOrgao())
            .configAmortizacao(configAmortizacao)
            .build();
    }

    private void salvar(Amortizacao amortizacao) {
        amortizacaoDataProvider.salvar(amortizacao);
    }

    private void atualizarValorLiquidoPatrimonio(Patrimonio patrimonio, QuotasConstantesOutputData quotasConstantes) {
        patrimonio.setValorLiquido(quotasConstantes.getValorPosteiror());
        patrimonioDataProvider.atualizar(patrimonio);
    }
}
