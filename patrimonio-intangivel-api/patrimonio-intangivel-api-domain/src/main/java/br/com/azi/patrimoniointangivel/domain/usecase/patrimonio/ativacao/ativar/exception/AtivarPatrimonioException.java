package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception;

public class AtivarPatrimonioException extends RuntimeException {
    public AtivarPatrimonioException() {
        super("Não é possível ativar patrimônio!");
    }
}
