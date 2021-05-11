package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.exception;

public class EscolheTipoException extends RuntimeException {

    public EscolheTipoException() {
        super("Não foi possivel selecionar o tipo do bem.");
    }
}
