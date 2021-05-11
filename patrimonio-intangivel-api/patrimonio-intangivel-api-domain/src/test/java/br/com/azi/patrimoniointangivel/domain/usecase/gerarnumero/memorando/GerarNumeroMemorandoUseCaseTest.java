package br.com.azi.patrimoniointangivel.domain.usecase.gerarnumero.memorando;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.converter.GerarNumeroMemorandoOutputDataConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class GerarNumeroMemorandoUseCaseTest {

    private PatrimonioDataProvider patrimonioDataProvider;

    @Before
    public void start() {
        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
    }

    @Test
    public void deveGerarNumeroDoPrimeiroMemorando(){
        GerarNumeroMemorandoUseCaseImpl useCase = new GerarNumeroMemorandoUseCaseImpl(
            patrimonioDataProvider,
            new GerarNumeroMemorandoOutputDataConverter());

        Mockito.when(patrimonioDataProvider.buscarUltimoPorMemorando()).thenReturn(Optional.of(Patrimonio.builder().build()));

        GerarNumeroMemorandoOutputData outputData = useCase.executar();

        Assert.assertEquals("000001", outputData.getNumeroMemorando());

    }

    @Test
    public void deveGerarNumeroDoProximoMemorando(){
        GerarNumeroMemorandoUseCaseImpl useCase = new GerarNumeroMemorandoUseCaseImpl(
            patrimonioDataProvider,
            new GerarNumeroMemorandoOutputDataConverter());

        Mockito.when(patrimonioDataProvider.buscarUltimoPorMemorando()).thenReturn(Optional.of(Patrimonio.builder().anoMemorando(2020).numeroMemorando("000001").build()));

        GerarNumeroMemorandoOutputData outputData = useCase.executar();

        Assert.assertEquals("000001", outputData.getNumeroMemorando());

    }

    @Test
    public void deveGerarPrimeiroNumeroDoProximoAno(){
        GerarNumeroMemorandoUseCaseImpl useCase = new GerarNumeroMemorandoUseCaseImpl(
            patrimonioDataProvider,
            new GerarNumeroMemorandoOutputDataConverter());

        Mockito.when(patrimonioDataProvider.buscarUltimoPorMemorando()).thenReturn(Optional.of(Patrimonio.builder().anoMemorando(2021).numeroMemorando("000005").build()));

        GerarNumeroMemorandoOutputData outputData = useCase.executar();

        Assert.assertEquals("000006", outputData.getNumeroMemorando());
    }
}
