package br.com.azi.patrimoniointangivel.domain.exception;

public class DocumentoNaoEncontradoException extends RuntimeException {
    public DocumentoNaoEncontradoException() {
        super("Documento não encontrado!");
    }
}
