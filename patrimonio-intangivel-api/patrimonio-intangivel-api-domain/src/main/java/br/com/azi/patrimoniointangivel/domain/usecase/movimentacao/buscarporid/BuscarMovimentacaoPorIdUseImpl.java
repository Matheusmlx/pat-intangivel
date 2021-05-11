package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarporid.converter.BuscarMovimentacaoPorIdOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarMovimentacaoPorIdUseImpl implements BuscarMovimentacaoPorIdUseCase{

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private BuscarMovimentacaoPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarMovimentacaoPorIdOutputData executar(BuscarMovimentacaoPorIdInputData inputData) {
        validarEntrada(inputData);
        Movimentacao movimentacao = buscarMovimentacao(inputData);

        return outputDataConverter.to(movimentacao);
    }

    private void validarEntrada(BuscarMovimentacaoPorIdInputData entrada){
        Validator.of(entrada)
            .validate(BuscarMovimentacaoPorIdInputData::getId, Objects::nonNull,"Id é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(BuscarMovimentacaoPorIdInputData idInputData){
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(idInputData.getId());
        return (movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new));
    }
}
