package br.com.azi.patrimoniointangivel.domain.exception;

public class PatrimonioNaoEncontradoException extends RuntimeException {

    public PatrimonioNaoEncontradoException() {
        super("Patrimonio não encontrado!");
    }
}
