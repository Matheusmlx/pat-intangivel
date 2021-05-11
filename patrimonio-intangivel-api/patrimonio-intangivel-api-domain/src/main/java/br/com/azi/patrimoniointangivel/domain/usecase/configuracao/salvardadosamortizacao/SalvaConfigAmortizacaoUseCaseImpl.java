package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao;


import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.converter.SalvaConfigAmortizacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.exception.VidaUtilException;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
public class SalvaConfigAmortizacaoUseCaseImpl implements SalvaConfigAmortizacaoUseCase {

    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    private SalvaConfigAmortizacaoOutputDataConverter outputDataConverter;

    private CalculaConfigAmortizacaoUseCase calculaConfigAmortizacaoUseCase;

    @Override
    public SalvaConfigAmortizacaoOutputData executar(SalvaConfigAmortizacaoInputData inputData) {

        validarDadosEntrada(inputData);

        ConfigAmortizacao configAmortizacao = retornaConfigAmortizacao(inputData);
        ConfigAmortizacao configAmortizacaoSalva = salvar(configAmortizacao);
        return outputDataConverter.to(configAmortizacaoSalva);
    }

    private ConfigAmortizacao retornaConfigAmortizacao(SalvaConfigAmortizacaoInputData inputData) {
        if (verificarContaAmortizavel(inputData)) {
            if (verificarContaCalculavel(inputData)) {
                validarVidaUtil(inputData);
                CalculaConfigAmortizacaoOutputData calculaConfigAmortizacaoOutputData = calcularDados(inputData);
                return criarEntidadeAmortizavelCalculavel(inputData, calculaConfigAmortizacaoOutputData);
            }
            return criarEntidadeAmortizavelNaoCalculavel(inputData);
        }
        return criarEntidadeNaoAmortizavel(inputData);
    }

    private void validarDadosEntrada(SalvaConfigAmortizacaoInputData input) {
        Validator.of(input)
            .validate(SalvaConfigAmortizacaoInputData::getTipo, Objects::nonNull, "Tipo é nula")
            .get();
    }

    private void validarVidaUtil(SalvaConfigAmortizacaoInputData inputData) {
        if (inputData.getVidaUtil() > 1200) {
            throw new VidaUtilException();
        }
    }

    private Boolean verificarContaCalculavel(SalvaConfigAmortizacaoInputData inputData) {
        return Objects.nonNull(inputData.getVidaUtil());
    }

    private Boolean verificarContaAmortizavel(SalvaConfigAmortizacaoInputData inputData) {
        return ConfigAmortizacao.Tipo.valueOf(inputData.getTipo()).equals(ConfigAmortizacao.Tipo.AMORTIZAVEL);
    }

    private CalculaConfigAmortizacaoOutputData calcularDados(SalvaConfigAmortizacaoInputData inputData) {
        CalculaConfigAmortizacaoInputData input = CalculaConfigAmortizacaoInputData
            .builder()
            .vidaUtil(inputData.getVidaUtil())
            .build();

        return calculaConfigAmortizacaoUseCase.executar(input);
    }

    private ConfigAmortizacao criarEntidadeAmortizavelCalculavel(SalvaConfigAmortizacaoInputData inputData, CalculaConfigAmortizacaoOutputData calculaConfigAmortizacaoOutputData) {
        return ConfigAmortizacao
            .builder()
            .contaContabil(ContaContabil.builder()
                .id(inputData.getContaContabil())
                .build())
            .vidaUtil(inputData.getVidaUtil())
            .situacao(ConfigAmortizacao.Situacao.ATIVO)
            .taxa(calculaConfigAmortizacaoOutputData.getTaxa())
            .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
            .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
            .percentualResidual(BigDecimal.ZERO)
            .build();
    }

    private ConfigAmortizacao criarEntidadeAmortizavelNaoCalculavel(SalvaConfigAmortizacaoInputData inputData) {
        return ConfigAmortizacao
            .builder()
            .contaContabil(ContaContabil.builder()
                .id(inputData.getContaContabil())
                .build())
            .situacao(ConfigAmortizacao.Situacao.ATIVO)
            .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
            .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
            .percentualResidual(BigDecimal.ZERO)
            .build();
    }

    private ConfigAmortizacao criarEntidadeNaoAmortizavel(SalvaConfigAmortizacaoInputData inputData) {
        return ConfigAmortizacao
            .builder()
            .tipo(ConfigAmortizacao.Tipo.valueOf(inputData.getTipo()))
            .metodo(null)
            .contaContabil(ContaContabil.builder().id(inputData.getContaContabil()).build())
            .situacao(ConfigAmortizacao.Situacao.ATIVO)
            .build();
    }

    private ConfigAmortizacao salvar(ConfigAmortizacao configAmortizacao) {
        return configAmortizacaoDataProvider.salvar(configAmortizacao);
    }
}
