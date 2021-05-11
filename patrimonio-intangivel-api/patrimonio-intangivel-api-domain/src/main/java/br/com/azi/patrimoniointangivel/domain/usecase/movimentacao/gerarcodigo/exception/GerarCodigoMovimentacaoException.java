package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.exception;

public class GerarCodigoMovimentacaoException extends  RuntimeException{
    public GerarCodigoMovimentacaoException(String message){
        super(message);
    }
}
