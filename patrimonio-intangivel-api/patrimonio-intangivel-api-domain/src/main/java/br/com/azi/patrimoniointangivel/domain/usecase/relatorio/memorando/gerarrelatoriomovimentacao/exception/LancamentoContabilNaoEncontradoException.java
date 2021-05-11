package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.exception;

public class LancamentoContabilNaoEncontradoException extends RuntimeException {
    public LancamentoContabilNaoEncontradoException() {
        super("Lançamento Contábil não encontrada!");
    }
}
