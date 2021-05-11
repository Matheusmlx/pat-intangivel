package br.com.azi.patrimoniointangivel.domain.exception;

public class TipoDocumentoNaoEncontradoException extends RuntimeException {

    public TipoDocumentoNaoEncontradoException() {
        super("Tipo de documento não encontrado!");
    }
}
