package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao;

import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.converter.GerarNumeroPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.exception.GerarNumeroPatrimonioException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class GerarNumeroPatrimonioTest {

    @Test
    public void deveGerarProximoNumerodePatrimonio() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        String digitos = "10";
        GerarNumeroPatrimonioUseCaseImpl useCase = new GerarNumeroPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            digitos,
            new GerarNumeroPatrimonioOutputDataConverter()
        );
        Mockito.when(patrimonioDataProvider.buscarUltimoAtivado())
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Alura")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .dataAtivacao(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.JANUARY.getValue()))
                .numero("0000000001")
                .build()));


        GerarNumeroPatrimonioOutputData outputData = useCase.executar();
        Assert.assertEquals("0000000002", outputData.getNumero());
    }

    @Test
    public void deveGerarPrimeiroNumerodePatrimonio() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        String digitos = "10";
        GerarNumeroPatrimonioUseCaseImpl useCase = new GerarNumeroPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            digitos,
            new GerarNumeroPatrimonioOutputDataConverter()
        );
        Mockito.when(patrimonioDataProvider.buscarUltimoAtivado())
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Alura")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build()));


        GerarNumeroPatrimonioOutputData outputData = useCase.executar();
        Assert.assertEquals("0000000001", outputData.getNumero());
    }

    @Test(expected = GerarNumeroPatrimonioException.class)
    public void deveFalharQuandoQuantidadeDeDigitosForaLimite() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        String digitos = "100";
        GerarNumeroPatrimonioUseCaseImpl useCase = new GerarNumeroPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            digitos,
            new GerarNumeroPatrimonioOutputDataConverter()
        );
        useCase.executar();
    }
}
