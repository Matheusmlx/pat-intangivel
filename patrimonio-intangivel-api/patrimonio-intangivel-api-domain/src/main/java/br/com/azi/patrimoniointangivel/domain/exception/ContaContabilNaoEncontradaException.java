package br.com.azi.patrimoniointangivel.domain.exception;

public class ContaContabilNaoEncontradaException extends RuntimeException {

    public ContaContabilNaoEncontradaException() {
        super("Conta contábil não encontrada!");
    }

}
