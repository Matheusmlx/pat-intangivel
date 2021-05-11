package br.com.azi.patrimoniointangivel.domain.exception;

public class IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException extends RuntimeException {

    public IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException() {
        super("O número da nota de lançamento contábil já está sendo utilizado.");
    }
}
