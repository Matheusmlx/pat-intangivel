package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.exception;

public class SituacaoEmElaboracaoException extends RuntimeException {
    public SituacaoEmElaboracaoException(){ super("Movimentações em elaboração não podem ser recebidas."); }
}
