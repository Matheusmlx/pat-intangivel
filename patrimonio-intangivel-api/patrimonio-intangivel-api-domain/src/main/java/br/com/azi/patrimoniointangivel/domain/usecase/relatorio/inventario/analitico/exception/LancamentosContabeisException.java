package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.exception;

public class LancamentosContabeisException extends RuntimeException{
    public LancamentosContabeisException() { super("Não foram encontrados lançamentos contábeis para esse órgão.");}
}
