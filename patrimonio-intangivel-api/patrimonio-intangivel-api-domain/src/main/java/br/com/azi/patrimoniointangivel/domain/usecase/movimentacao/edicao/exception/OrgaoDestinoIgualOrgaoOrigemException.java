package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.exception;

public class OrgaoDestinoIgualOrgaoOrigemException extends RuntimeException {
    public OrgaoDestinoIgualOrgaoOrigemException() { super("Órgão destino deve ser diferente do órgão de origem!");}
}
