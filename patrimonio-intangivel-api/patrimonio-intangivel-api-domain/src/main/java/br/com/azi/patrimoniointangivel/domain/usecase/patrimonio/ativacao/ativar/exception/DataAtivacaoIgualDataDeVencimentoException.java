package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception;

public class DataAtivacaoIgualDataDeVencimentoException extends RuntimeException{
    public DataAtivacaoIgualDataDeVencimentoException(){
        super("A data de Vencimento deve ser posterior a data de Ativação");
    }
}
