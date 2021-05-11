package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.expection;

public class QuantidadeDeDocumentosCadastradosExcedidoException extends RuntimeException {
    public QuantidadeDeDocumentosCadastradosExcedidoException() {
        super("A quantidade máxima de documentos cadastrados para este patrimônio foi atingida.");
    }
}
