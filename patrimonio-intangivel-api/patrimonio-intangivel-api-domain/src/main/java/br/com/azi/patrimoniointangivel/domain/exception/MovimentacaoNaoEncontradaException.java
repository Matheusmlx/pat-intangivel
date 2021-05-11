package br.com.azi.patrimoniointangivel.domain.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException{
    public MovimentacaoNaoEncontradaException(){ super("Movimentação não encontrada!"); }
}
