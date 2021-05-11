package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

public class BuscarFornecedorException extends RuntimeException {
    public BuscarFornecedorException(String message) {
        super(message);
    }

    public BuscarFornecedorException(String message, Throwable cause) {
        super(message, cause);
    }
}
