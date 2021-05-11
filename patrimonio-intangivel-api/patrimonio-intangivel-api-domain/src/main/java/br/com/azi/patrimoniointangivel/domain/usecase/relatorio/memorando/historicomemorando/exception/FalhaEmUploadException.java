package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.exception;

public class FalhaEmUploadException extends RuntimeException {
    public FalhaEmUploadException() {
        super("Não foi possível realizar o upload.");
    }
}
