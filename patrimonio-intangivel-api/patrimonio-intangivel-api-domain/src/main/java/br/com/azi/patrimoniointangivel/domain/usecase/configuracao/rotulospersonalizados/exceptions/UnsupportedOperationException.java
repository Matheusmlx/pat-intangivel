package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.rotulospersonalizados.exceptions;

public class UnsupportedOperationException extends RuntimeException {

    public UnsupportedOperationException() {
        super("Essa operação não é permitida!");
    }
}
