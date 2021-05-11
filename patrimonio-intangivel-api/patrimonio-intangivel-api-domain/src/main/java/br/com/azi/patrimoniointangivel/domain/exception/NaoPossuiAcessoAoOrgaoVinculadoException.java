package br.com.azi.patrimoniointangivel.domain.exception;

public class NaoPossuiAcessoAoOrgaoVinculadoException extends RuntimeException {

    public NaoPossuiAcessoAoOrgaoVinculadoException() {
        super("Você não possui acesso ao orgão vinculado a este patrimonio!");
    }
}
