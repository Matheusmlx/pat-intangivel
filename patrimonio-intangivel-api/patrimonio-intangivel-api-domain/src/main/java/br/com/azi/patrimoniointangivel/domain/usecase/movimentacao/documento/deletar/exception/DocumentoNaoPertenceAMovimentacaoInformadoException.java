package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.deletar.exception;

public class DocumentoNaoPertenceAMovimentacaoInformadoException extends RuntimeException {

    public DocumentoNaoPertenceAMovimentacaoInformadoException() {
        super("Este documento não pertence a movimentação informada");
    }
}
