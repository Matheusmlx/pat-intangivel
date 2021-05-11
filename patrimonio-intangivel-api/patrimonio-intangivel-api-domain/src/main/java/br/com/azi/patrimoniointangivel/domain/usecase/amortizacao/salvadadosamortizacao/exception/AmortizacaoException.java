package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.exception;

public class AmortizacaoException extends RuntimeException {
    public AmortizacaoException() {
        super("Vida Util Não Válida!");
    }
}
