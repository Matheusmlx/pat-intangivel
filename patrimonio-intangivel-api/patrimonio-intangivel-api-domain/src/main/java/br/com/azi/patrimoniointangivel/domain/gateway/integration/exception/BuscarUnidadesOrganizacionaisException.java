package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

public class BuscarUnidadesOrganizacionaisException extends RuntimeException {
    public BuscarUnidadesOrganizacionaisException(String message) {
        super(message);
    }

    public BuscarUnidadesOrganizacionaisException(String message, Throwable cause) {
        super(message, cause);
    }
}
