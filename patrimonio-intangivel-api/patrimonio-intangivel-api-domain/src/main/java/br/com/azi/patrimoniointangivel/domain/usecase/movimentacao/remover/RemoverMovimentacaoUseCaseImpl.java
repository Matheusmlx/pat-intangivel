package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.exception.SituacaoMovimentacaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class RemoverMovimentacaoUseCaseImpl implements RemoverMovimentacaoUseCase {

    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Override
    public void executar(RemoverMovimentacaoInputData inputData) {

        validarEntrada(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData);
        validarSeEstadoDeMovimentacaoEmElaboracao(movimentacao);

        removerMovimentacao(movimentacao);
    }

    private void validarEntrada(RemoverMovimentacaoInputData inputData){
        Validator.of(inputData)
            .validate(RemoverMovimentacaoInputData::getId, Objects::nonNull,"Id da Movimentação é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(RemoverMovimentacaoInputData inputData){
        Optional<Movimentacao> movimentacao= movimentacaoDataProvider.buscarPorId(inputData.getId());
        return (movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new));

    }

    private void validarSeEstadoDeMovimentacaoEmElaboracao(Movimentacao movimentacao){
        if(movimentacao.getSituacao() != Movimentacao.Situacao.EM_ELABORACAO){
            throw new SituacaoMovimentacaoException();
        }
    }

    private void removerMovimentacao(Movimentacao movimentacao){
        movimentacaoDataProvider.remover(movimentacao.getId());
    }

}
