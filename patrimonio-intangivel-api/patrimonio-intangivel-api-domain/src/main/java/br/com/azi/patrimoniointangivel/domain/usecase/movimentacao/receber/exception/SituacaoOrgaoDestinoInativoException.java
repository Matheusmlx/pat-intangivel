package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.exception;

public class SituacaoOrgaoDestinoInativoException extends RuntimeException{
    public SituacaoOrgaoDestinoInativoException (){ super("O órgão de destino está inativo. Essa movimentação não pode ser recebida");}
}
