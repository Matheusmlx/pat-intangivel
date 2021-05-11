package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.rejeitar;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class RejeitarMovimentacaoUseCaseImpl implements RejeitarMovimentacaoUseCase {

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private UsuarioDataProvider usuarioDataProvider;

    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;


    private Clock clock;

    @Override
    public void executar(RejeitarMovimentacaoInputData inputData) {
        validarEntrada(inputData);
        validarSeMovimentacaoExiste(inputData);
        Movimentacao movimentacao = buscar(inputData);

        registrarDataHoraDeRejeicao(movimentacao);
        atualizarSituacao(movimentacao);
        setarUsuarioFinalizacao(movimentacao);

        salvar(movimentacao);
    }

    private void validarEntrada(RejeitarMovimentacaoInputData inputData){
        Validator.of(inputData)
            .validate(RejeitarMovimentacaoInputData::getId, Objects::nonNull,"Id da movimentação é nulo")
            .get();
    }

    private void validarSeMovimentacaoExiste(RejeitarMovimentacaoInputData inputData){
        if(!movimentacaoDataProvider.existe(inputData.getId())){
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscar(RejeitarMovimentacaoInputData inputData){
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return (movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new));
    }

    private void registrarDataHoraDeRejeicao(Movimentacao movimentacao) {
        movimentacao.setDataDeFinalizacao(LocalDateTime.now(clock));
    }

    private void atualizarSituacao(Movimentacao movimentacao) {
        movimentacao.setSituacao(Movimentacao.Situacao.REJEITADO);
    }

    private void setarUsuarioFinalizacao(Movimentacao movimentacao){
        movimentacao.setUsuarioFinalizacao(usuarioDataProvider.buscarUsuarioPorSessao(sessaoUsuarioDataProvider.get()).getNome());
    }

    private Movimentacao salvar(Movimentacao movimentacao){
        return movimentacaoDataProvider.salvar(movimentacao);
    }
}
