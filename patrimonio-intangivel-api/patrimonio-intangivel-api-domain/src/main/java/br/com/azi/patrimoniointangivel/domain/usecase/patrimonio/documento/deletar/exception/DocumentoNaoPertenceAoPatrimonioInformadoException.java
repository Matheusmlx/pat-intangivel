package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.deletar.exception;

public class DocumentoNaoPertenceAoPatrimonioInformadoException extends RuntimeException {

    public DocumentoNaoPertenceAoPatrimonioInformadoException() {
        super("Este documento não pertence ao patrimonio informado");
    }
}
