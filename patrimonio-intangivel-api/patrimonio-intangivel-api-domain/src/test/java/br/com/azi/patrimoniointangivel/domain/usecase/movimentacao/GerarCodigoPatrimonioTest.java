package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.converter.GerarCodigoDemovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.exception.GerarCodigoMovimentacaoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class GerarCodigoPatrimonioTest {

    @Test
    public void deveGerarProximoCodigoDeMovimentacao(){
        MovimentacaoDataProvider movimentacaoDataProvider =  Mockito.mock(MovimentacaoDataProvider.class);
        String quantidadeDigitos = "5";
        GerarCodigoDeMovimentacaoUseCaseImpl useCase = new GerarCodigoDeMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            quantidadeDigitos,
            new GerarCodigoDemovimentacaoOutputDataConverter()
        );

        Mockito.when(movimentacaoDataProvider.buscarUltimoCriado())
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                .tipo(Movimentacao.Tipo.DOACAO_PARA_TERCEIROS)
                .codigo("00001")
                .build()));

        GerarCodigoDeMovimentacaoOutputData outputData = useCase.executar();
        Assert.assertEquals("00002",outputData.getCodigo());

    }

    @Test
    public void deveGerarPrimeiroCodigoDaMovimentacao(){
        MovimentacaoDataProvider movimentacaoDataProvider =  Mockito.mock(MovimentacaoDataProvider.class);
        String quantidadeDigitos = "5";
        GerarCodigoDeMovimentacaoUseCaseImpl useCase = new GerarCodigoDeMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            quantidadeDigitos,
            new GerarCodigoDemovimentacaoOutputDataConverter()
        );
        Mockito.when(movimentacaoDataProvider.buscarUltimoCriado())
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(1L)
                .situacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)
                .tipo(Movimentacao.Tipo.DOACAO_PARA_TERCEIROS)
                .build()));
        GerarCodigoDeMovimentacaoOutputData outputData = useCase.executar();
        Assert.assertEquals("00001",outputData.getCodigo());
    }

    @Test(expected = GerarCodigoMovimentacaoException.class)
    public void deveFalharComQuantidadeDeDigitosForaDoLimite(){
        MovimentacaoDataProvider movimentacaoDataProvider =  Mockito.mock(MovimentacaoDataProvider.class);
        String quantidadeDigitos = "150";
        GerarCodigoDeMovimentacaoUseCaseImpl useCase = new GerarCodigoDeMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            quantidadeDigitos,
            new GerarCodigoDemovimentacaoOutputDataConverter()
        );

        useCase.executar();
    }
}
