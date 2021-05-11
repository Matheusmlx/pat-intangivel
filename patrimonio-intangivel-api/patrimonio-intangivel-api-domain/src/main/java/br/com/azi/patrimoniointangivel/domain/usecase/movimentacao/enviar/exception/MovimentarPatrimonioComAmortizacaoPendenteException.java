package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.exception;

public class MovimentarPatrimonioComAmortizacaoPendenteException extends RuntimeException{
    public MovimentarPatrimonioComAmortizacaoPendenteException() {
        super("Não é possível movimentar patrimônios com amortizações pendentes!");
    }
}
