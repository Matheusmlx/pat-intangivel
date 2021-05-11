package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception;

public class OrgaoNaoEncontradoException extends RuntimeException {
    public OrgaoNaoEncontradoException() { super("Não foi possível encontrar o órgão!"); }
}
