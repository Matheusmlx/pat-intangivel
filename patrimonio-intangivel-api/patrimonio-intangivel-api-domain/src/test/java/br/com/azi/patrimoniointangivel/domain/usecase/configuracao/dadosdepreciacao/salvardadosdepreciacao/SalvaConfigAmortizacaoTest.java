package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.dadosdepreciacao.salvardadosdepreciacao;


import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.calculadadosamortizacao.CalculaConfigAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.converter.SalvaConfigAmortizacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.exception.VidaUtilException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SalvaConfigAmortizacaoTest {

    @Test
    public void deveSalvarDadosAmortizacao() {
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        CalculaConfigAmortizacaoUseCase calculaConfigAmortizacaoUseCase = Mockito.mock(CalculaConfigAmortizacaoUseCase.class);
        SalvaConfigAmortizacaoUseCaseImpl useCase = new SalvaConfigAmortizacaoUseCaseImpl(
            configAmortizacaoDataProvider,
            new SalvaConfigAmortizacaoOutputDataConverter(),
            calculaConfigAmortizacaoUseCase
        );

        SalvaConfigAmortizacaoInputData inputData = SalvaConfigAmortizacaoInputData
            .builder()
            .vidaUtil((short) 60)
            .tipo("AMORTIZAVEL")
            .contaContabil(1L)
            .build();

        Mockito.when(calculaConfigAmortizacaoUseCase.executar(CalculaConfigAmortizacaoInputData.builder().vidaUtil((short) 60).build()))
            .thenReturn(CalculaConfigAmortizacaoOutputData
                .builder()
                .taxa(new BigDecimal(1.666666666667).setScale(12, RoundingMode.HALF_EVEN))
                .build());

        Mockito.when(configAmortizacaoDataProvider.salvar(any(ConfigAmortizacao.class)))
            .thenReturn(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
                .sistema("Intangivel")
                .contaContabil(ContaContabil.builder().id(1L).build())
                .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                .vidaUtil((short) 60)
                .situacao(ConfigAmortizacao.Situacao.ATIVO)
                .taxa(new BigDecimal(1.666666666667).setScale(12, RoundingMode.HALF_EVEN))
                .percentualResidual(BigDecimal.valueOf(0.1))
                .build());


        SalvaConfigAmortizacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("AMORTIZAVEL", outputData.getTipo());
        Assert.assertEquals("Intangivel", outputData.getSistema());
        Assert.assertEquals("QUOTAS_CONSTANTES", outputData.getMetodo());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
        Assert.assertEquals(new BigDecimal(1.666666666667).setScale(12, RoundingMode.HALF_EVEN), outputData.getTaxa());
        Assert.assertEquals(BigDecimal.valueOf(0.1), outputData.getPercentualResidual());

    }

    @Test
    public void deveSalvarConfiContaNaoCalculavel() {
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        CalculaConfigAmortizacaoUseCase calculaConfigAmortizacaoUsecase = Mockito.mock(CalculaConfigAmortizacaoUseCase.class);
        SalvaConfigAmortizacaoUseCaseImpl useCase = new SalvaConfigAmortizacaoUseCaseImpl(
            configAmortizacaoDataProvider,
            new SalvaConfigAmortizacaoOutputDataConverter(),
            calculaConfigAmortizacaoUsecase
        );

        SalvaConfigAmortizacaoInputData inputData = SalvaConfigAmortizacaoInputData
            .builder()
            .contaContabil(1L)
            .tipo(String.valueOf(ConfigAmortizacao.Tipo.AMORTIZAVEL))
            .build();


        Mockito.when(configAmortizacaoDataProvider.salvar(any(ConfigAmortizacao.class)))
            .thenReturn(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
                .contaContabil(ContaContabil.builder().id(1L).build())
                .situacao(ConfigAmortizacao.Situacao.ATIVO)
                .build()
            );

        SalvaConfigAmortizacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("AMORTIZAVEL", outputData.getTipo());
        Assert.assertNull(outputData.getMetodo());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
    }

    @Test
    public void deveSalvarEntidadeNaoAmortizavel() {
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        CalculaConfigAmortizacaoUseCase calculaConfigAmortizacaoUsecase = Mockito.mock(CalculaConfigAmortizacaoUseCase.class);
        SalvaConfigAmortizacaoUseCaseImpl useCase = new SalvaConfigAmortizacaoUseCaseImpl(
            configAmortizacaoDataProvider,
            new SalvaConfigAmortizacaoOutputDataConverter(),
            calculaConfigAmortizacaoUsecase
        );

        SalvaConfigAmortizacaoInputData inputData = SalvaConfigAmortizacaoInputData
            .builder()
            .contaContabil(1L)
            .tipo(String.valueOf(ConfigAmortizacao.Tipo.NAO_AMORTIZAVEL))
            .build();


        Mockito.when(configAmortizacaoDataProvider.salvar(any(ConfigAmortizacao.class)))
            .thenReturn(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.NAO_AMORTIZAVEL)
                .contaContabil(ContaContabil.builder().id(1L).build())
                .situacao(ConfigAmortizacao.Situacao.ATIVO)
                .build()
            );

        SalvaConfigAmortizacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("NAO_AMORTIZAVEL", outputData.getTipo());
        Assert.assertNull(outputData.getMetodo());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
    }

    @Test(expected = VidaUtilException.class)
    public void deveFalharQuandoVidaUtilMaiorQuePermitida() {
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        CalculaConfigAmortizacaoUseCase calculaConfigAmortizacaoUsecase = Mockito.mock(CalculaConfigAmortizacaoUseCase.class);
        SalvaConfigAmortizacaoUseCaseImpl useCase = new SalvaConfigAmortizacaoUseCaseImpl(
            configAmortizacaoDataProvider,
            new SalvaConfigAmortizacaoOutputDataConverter(),
            calculaConfigAmortizacaoUsecase
        );

        SalvaConfigAmortizacaoInputData inputData = SalvaConfigAmortizacaoInputData
            .builder()
            .contaContabil(1L)
            .vidaUtil((short) 13000)
            .tipo(String.valueOf(ConfigAmortizacao.Tipo.AMORTIZAVEL))
            .build();

       useCase.executar(inputData);
    }

}
