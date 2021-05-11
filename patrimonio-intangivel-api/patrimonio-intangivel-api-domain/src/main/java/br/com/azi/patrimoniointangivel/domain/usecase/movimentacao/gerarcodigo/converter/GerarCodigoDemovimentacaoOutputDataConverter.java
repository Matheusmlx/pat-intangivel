package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.converter;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoOutputData;

public class GerarCodigoDemovimentacaoOutputDataConverter {
    public GerarCodigoDeMovimentacaoOutputData to(String codigo){
        GerarCodigoDeMovimentacaoOutputData gerarCodigoDeMovimentacaoOutputData = new GerarCodigoDeMovimentacaoOutputData();
        gerarCodigoDeMovimentacaoOutputData.setCodigo(codigo);
        return gerarCodigoDeMovimentacaoOutputData;
    }

}
