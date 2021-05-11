package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.exception;

public class DesativarPatrimonioException extends RuntimeException {

    public DesativarPatrimonioException() {
        super("Patrimônio não pode ser desativado!");
    }
}
