package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.exception;

public class EditarDocumentoException extends RuntimeException {
    public EditarDocumentoException() {
        super("Não foi possível editar documento!");
    }
}
