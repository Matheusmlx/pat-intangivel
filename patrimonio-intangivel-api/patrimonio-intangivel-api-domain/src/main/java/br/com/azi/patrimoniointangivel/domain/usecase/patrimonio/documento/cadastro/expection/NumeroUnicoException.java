package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.expection;

public class NumeroUnicoException extends RuntimeException {
    public NumeroUnicoException() {
        super("Já existe um documento com esse número");
    }

}
