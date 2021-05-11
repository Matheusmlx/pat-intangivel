package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception;

public class EditarPatrimonioAtivoException extends RuntimeException {

    public EditarPatrimonioAtivoException() {
        super("Não é possível editar patrimônios ativos!");
    }
}
