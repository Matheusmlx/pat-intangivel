package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.exception;

public class RodarAmortizacaoException extends RuntimeException {
    public RodarAmortizacaoException() {
        super("Erro ao rodar amortização!");
    }
}
