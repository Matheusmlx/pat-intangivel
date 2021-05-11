package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.remocao.exception;

public class PatrimonioNaoPodeSerExcluidoException extends RuntimeException {
    public PatrimonioNaoPodeSerExcluidoException() {
        super("Patrimônio não pode ser removido.");
    }
}
