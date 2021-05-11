package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.exception.ContaContabilNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.converter.BuscarContaContabilPorIdOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarContaContabilPorIdUseCaseImpl implements BuscarContaContabilPorIdUseCase {

    ContaContabilDataProvider contaContabilDataProvider;

    ConfigContaContabilDataProvider configContaContabilDataProvider;

    BuscarContaContabilPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarContaContabilPorIdOutputData executar(BuscarContaContabilPorIdInputData inputData) {
        validarDadosEntrada(inputData);

        ContaContabil entidadeEncontrada = buscar(inputData);
        carregarConfigAmortizacao(entidadeEncontrada);

        return outputDataConverter.to(entidadeEncontrada);
    }

    private void validarDadosEntrada(BuscarContaContabilPorIdInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarContaContabilPorIdInputData::getId, Objects::nonNull, "O id é nulo")
            .validate(BuscarContaContabilPorIdInputData::getProdutoId, Objects::nonNull, "O id do produto é nulo")
            .get();
    }

    private ContaContabil buscar(BuscarContaContabilPorIdInputData inputData) {
        Optional<ContaContabil> entidade = contaContabilDataProvider.buscarPorId(inputData.getId());
        return entidade.orElseThrow(ContaContabilNaoEncontradaException::new);
    }

    private void carregarConfigAmortizacao(ContaContabil entidade) {
        Optional<ConfigContaContabil> configAmortizacao = configContaContabilDataProvider.buscarAtualPorContaContabil(entidade.getId());
        configAmortizacao.ifPresent(depreciacao -> {
            entidade.setConfigContaContabil(configAmortizacao.get());
        });
    }
}
