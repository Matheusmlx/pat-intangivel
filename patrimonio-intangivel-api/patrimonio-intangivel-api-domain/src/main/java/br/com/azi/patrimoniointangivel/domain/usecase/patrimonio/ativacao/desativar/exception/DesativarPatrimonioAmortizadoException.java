package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.exception;

public class DesativarPatrimonioAmortizadoException extends RuntimeException {
    public DesativarPatrimonioAmortizadoException() {
        super("Não é possível cancelar a ativação desse patrimônio porque os dados de amortização já foram criados.");
    }
}
