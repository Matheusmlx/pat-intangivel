package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.remover.exception;

public class SituacaoMovimentacaoException extends RuntimeException {
    public SituacaoMovimentacaoException(){ super("Não e permitido exluir movimentações que não se encontram em elaboração"); }
}
