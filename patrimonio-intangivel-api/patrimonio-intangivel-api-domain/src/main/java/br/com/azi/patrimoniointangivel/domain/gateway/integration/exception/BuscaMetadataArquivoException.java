package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

public class BuscaMetadataArquivoException extends RuntimeException {
    public BuscaMetadataArquivoException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
