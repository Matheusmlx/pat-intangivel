package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoOutputData;

public class GerarNumeroMemorandoOutputDataConverter {
    public GerarNumeroMemorandoOutputData to(String numero) {
        GerarNumeroMemorandoOutputData gerarNumeroMemorandoOutputData = new GerarNumeroMemorandoOutputData();
        gerarNumeroMemorandoOutputData.setNumeroMemorando(numero);
        return gerarNumeroMemorandoOutputData;
    }
}
