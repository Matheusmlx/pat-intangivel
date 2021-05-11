package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.exception;

public class FiltroIncompletoException extends RuntimeException {
    public FiltroIncompletoException(String message) {
        super(message);
    }
}
