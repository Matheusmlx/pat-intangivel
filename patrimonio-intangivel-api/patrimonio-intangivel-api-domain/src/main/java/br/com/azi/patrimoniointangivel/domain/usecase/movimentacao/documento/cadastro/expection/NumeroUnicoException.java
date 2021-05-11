package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.expection;

public class NumeroUnicoException extends RuntimeException{
    public NumeroUnicoException(){
        super("Já existe um documento com esse número");
    }
}
