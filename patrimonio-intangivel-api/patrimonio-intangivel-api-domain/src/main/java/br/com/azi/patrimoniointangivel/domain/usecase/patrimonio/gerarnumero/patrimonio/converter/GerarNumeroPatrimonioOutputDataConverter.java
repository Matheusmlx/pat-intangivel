package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.converter;


import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioOutputData;

public class GerarNumeroPatrimonioOutputDataConverter {
    public GerarNumeroPatrimonioOutputData to(String numero) {
        GerarNumeroPatrimonioOutputData gerarNumeroPatrimonioOutputData = new GerarNumeroPatrimonioOutputData();
        gerarNumeroPatrimonioOutputData.setNumero(numero);
        return gerarNumeroPatrimonioOutputData;
    }
}
