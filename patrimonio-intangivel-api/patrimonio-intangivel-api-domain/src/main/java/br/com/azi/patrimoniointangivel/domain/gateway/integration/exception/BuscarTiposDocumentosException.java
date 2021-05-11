package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

public class BuscarTiposDocumentosException extends RuntimeException {
    public BuscarTiposDocumentosException(String message) {
        super(message);
    }

    public BuscarTiposDocumentosException(String message, Throwable cause) {
        super(message, cause);
    }
}
