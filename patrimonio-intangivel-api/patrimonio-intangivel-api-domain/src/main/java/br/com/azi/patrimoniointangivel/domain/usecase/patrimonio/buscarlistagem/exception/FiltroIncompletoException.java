package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.exception;

public class FiltroIncompletoException extends RuntimeException {
    public FiltroIncompletoException(String message) {
        super(message);
    }
}
