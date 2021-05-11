package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.exception;

public class TaxaInvalidaException extends RuntimeException {
    public TaxaInvalidaException() {
        super("Taxa informada não é válida!");
    }
}
