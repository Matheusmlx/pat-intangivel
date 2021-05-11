package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

public class RemocaoArquivoException extends RuntimeException {
    public RemocaoArquivoException(String mensagem) {
        super(mensagem);
    }

    public RemocaoArquivoException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
