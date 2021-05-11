package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception;

public class BuscaRelatorioException extends RuntimeException {
    public BuscaRelatorioException() {
        super("Não há registros encontrados.");
    }
}
