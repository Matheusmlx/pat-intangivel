package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.expection;

public class QuantidadeDeDocumentosCadastradoExcedidoException extends RuntimeException {
    public QuantidadeDeDocumentosCadastradoExcedidoException() {
        super("A quantidade máxima de documentos cadastrados para esta Movimentação foi atingida.");
    }
}
