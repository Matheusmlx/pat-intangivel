package br.com.azi.patrimoniointangivel.domain.gateway.integration.exception;

public class DownloadArquivoException extends RuntimeException {
    public DownloadArquivoException(String mensagem) {
        super(mensagem);
    }

    public DownloadArquivoException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
