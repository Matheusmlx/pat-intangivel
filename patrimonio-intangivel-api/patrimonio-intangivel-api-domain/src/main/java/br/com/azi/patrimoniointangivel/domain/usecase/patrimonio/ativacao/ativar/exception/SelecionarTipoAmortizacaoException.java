package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception;


public class SelecionarTipoAmortizacaoException extends RuntimeException {
    public SelecionarTipoAmortizacaoException() {
        super("Configure o tipo de conta contábil antes de usá-la em um patrimônio.");
    }
}
