package br.com.azi.patrimoniointangivel.utils.validate.exception;

public class NotaLancamentoContabilComDataNoFuturoException extends RuntimeException {

    public NotaLancamentoContabilComDataNoFuturoException() {
        super("A data da nota de lançamento contábil não pode ser uma data no futuro.");
    }
}

