package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.exception;

public class NumeroUnicoException extends RuntimeException {
    public NumeroUnicoException() {
        super("Já existe um documento com esse número");
    }
}
