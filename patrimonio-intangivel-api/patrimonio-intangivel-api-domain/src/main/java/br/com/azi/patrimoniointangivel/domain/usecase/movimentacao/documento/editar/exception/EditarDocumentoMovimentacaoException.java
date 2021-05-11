package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.exception;

public class EditarDocumentoMovimentacaoException extends RuntimeException {
    public EditarDocumentoMovimentacaoException() {
        super("Não foi possível editar documento!");
    }
}
