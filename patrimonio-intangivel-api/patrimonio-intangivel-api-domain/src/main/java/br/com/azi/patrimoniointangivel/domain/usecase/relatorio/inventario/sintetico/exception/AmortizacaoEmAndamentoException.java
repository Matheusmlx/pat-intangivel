package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception;

public class AmortizacaoEmAndamentoException extends RuntimeException {
    public AmortizacaoEmAndamentoException() {
        super("Inventário não pode ser gerado porque a rotina de amortização está rodando. Tente novamente em algumas horas.");
    }
}
