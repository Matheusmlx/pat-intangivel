package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception;

public class PatrimonioNomeInvalidoException extends RuntimeException {
    public PatrimonioNomeInvalidoException() {
        super("Nome com mais de 100 caracteres!");
    }
}
