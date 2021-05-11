package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.converter.IniciaAmortizacaoManualOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.exception.AmortizacaoManualException;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.Silent.class)
public class IniciaAmortizacaoManualUseCaseTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 6, 1);
    private PatrimonioDataProvider patrimonioDataProvider;
    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;
    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;
    private SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase;
    private AmortizacaoDataProvider amortizacaoDataProvider;
    private Clock fixedClock;

    @Before
    public void start() {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);
        salvaDadosAmortizacaoUseCase = Mockito.mock(SalvaDadosAmortizacaoUseCase.class);
        amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
    }


    @Test
    public void deveRetornarSomentePatrimoniosAmortizados() {

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveisPorOrgao(any(Long.class)))
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Deve Amortizar Este")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao
                                .builder()
                                .id(1L)
                                .build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .id(9L)
                            .build())
                        .dataAtivacao(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.JANUARY.getValue()))
                        .numero("0000000001")
                        .build(),
                    Patrimonio.builder()
                        .id(2L)
                        .nome("Deve Amortizar Este")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional.builder().id(9L).build())
                        .dataAtivacao(LocalDateTime.now()
                            .withYear(2020)
                            .withMonth(Month.FEBRUARY.getValue()))
                        .numero("0000000002")
                        .build(),
                    Patrimonio.builder()
                        .id(3L)
                        .nome("Não Deve Amortizar Este")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional.builder().id(9L).build())
                        .dataAtivacao(LocalDateTime.now()
                            .withYear(2020)
                            .withMonth(Month.APRIL.getValue()))
                        .numero("0000000003")
                        .build(),
                    Patrimonio.builder()
                        .id(4L)
                        .nome("Não Deve Amortizar Este")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withMonth(Month.APRIL.getValue())
                            .withDayOfMonth(30))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withMonth(Month.APRIL.getValue())
                            .withDayOfMonth(30))
                        .orgao(UnidadeOrganizacional.builder().id(9L).build())
                        .dataAtivacao(LocalDateTime.now()
                            .withDayOfMonth(2)
                            .withYear(2020)
                            .withMonth(Month.APRIL.getValue()))
                        .numero("0000000004")
                        .build(),
                    Patrimonio.builder()
                        .id(5L)
                        .nome("Deve Amortizar Este")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withMonth(Month.SEPTEMBER.getValue())
                            .withDayOfMonth(30))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withMonth(Month.SEPTEMBER.getValue())
                            .withDayOfMonth(30))
                        .orgao(UnidadeOrganizacional.builder().id(9L).build())
                        .dataAtivacao(LocalDateTime.now()
                            .withDayOfMonth(2)
                            .withYear(2020)
                            .withMonth(Month.FEBRUARY.getValue()))
                        .numero("0000000005")
                        .build()));

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        Mockito.when(configAmortizacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
                .sistema("Intangivel")
                .contaContabil(ContaContabil.builder().id(1L).build())
                .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                .vidaUtil((short) 60)
                .situacao(ConfigAmortizacao.Situacao.ATIVO)
                .taxa(new BigDecimal("1.666666666667").setScale(12, RoundingMode.HALF_EVEN))
                .percentualResidual(BigDecimal.valueOf(0))
                .build()));

        IniciaAmortizacaoManualUseCaseImpl usecase = new IniciaAmortizacaoManualUseCaseImpl(patrimonioDataProvider,
            configAmortizacaoDataProvider,
            dadosAmortizacaoDataProvider,
            salvaDadosAmortizacaoUseCase,
            amortizacaoDataProvider,
            fixedClock,
            new IniciaAmortizacaoManualOutputDataConverter());

        IniciaAmortizacaoManualOutputData outputData = usecase.executar(new IniciaAmortizacaoManualInputData((long) 9, "03", "2020"));

        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(3, outputData.getItems().size());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
        Assert.assertEquals(java.util.Optional.of(2L).get(), outputData.getItems().get(1).getId());
        Assert.assertEquals(java.util.Optional.of(5L).get(), outputData.getItems().get(2).getId());
        Assert.assertEquals("0000000001", outputData.getItems().get(0).getNumero());
        Assert.assertEquals("0000000002", outputData.getItems().get(1).getNumero());
        Assert.assertEquals("0000000005", outputData.getItems().get(2).getNumero());

    }

    @Test(expected = AmortizacaoManualException.class)
    public void deveFalharQuandoNaoTiverPatrimonios() {

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveisPorOrgao(any(Long.class)))
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional.builder().id(9L).build())
                        .dataAtivacao(LocalDateTime.now()
                            .withYear(2020)
                            .withMonth(Month.APRIL.getValue()))
                        .numero("0000000001")
                        .build()
                )

            );


        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        Mockito.when(configAmortizacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
                .sistema("Intangivel")
                .contaContabil(ContaContabil.builder().id(1L).build())
                .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                .vidaUtil((short) 60)
                .situacao(ConfigAmortizacao.Situacao.ATIVO)
                .taxa(new BigDecimal("1.666666666667").setScale(12, RoundingMode.HALF_EVEN))
                .percentualResidual(BigDecimal.valueOf(0))
                .build()));

        Mockito.when(amortizacaoDataProvider.existePorPatrimonioNoPeriodo(any(Long.class), any(LocalDateTime.class)))
            .thenReturn(Boolean.TRUE);


        IniciaAmortizacaoManualUseCaseImpl usecase = new IniciaAmortizacaoManualUseCaseImpl(patrimonioDataProvider,
            configAmortizacaoDataProvider,
            dadosAmortizacaoDataProvider,
            salvaDadosAmortizacaoUseCase,
            amortizacaoDataProvider,
            fixedClock,
            new IniciaAmortizacaoManualOutputDataConverter());

        usecase.executar(new IniciaAmortizacaoManualInputData((long) 9, "04", "2020"));
    }

    @Test(expected = AmortizacaoManualException.class)
    public void deveFalharNoMesCorrente() {
        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveisPorOrgao(any(Long.class)))
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional.builder().id(9L).build())
                        .dataAtivacao(LocalDateTime.now()
                            .withYear(2020)
                            .withMonth(Month.APRIL.getValue()))
                        .numero("0000000001")
                        .build()
                )

            );

        IniciaAmortizacaoManualUseCaseImpl usecase = new IniciaAmortizacaoManualUseCaseImpl(patrimonioDataProvider,
            configAmortizacaoDataProvider,
            dadosAmortizacaoDataProvider,
            salvaDadosAmortizacaoUseCase,
            amortizacaoDataProvider,
            fixedClock,
            new IniciaAmortizacaoManualOutputDataConverter());

        IniciaAmortizacaoManualInputData inputData = new IniciaAmortizacaoManualInputData();
        inputData.setAno("2020");
        inputData.setMes("06");
        inputData.setOrgao(9L);

        usecase.executar(inputData);
    }

    @Test
    public void deveAmortizarPatrimonioAcumulado() {
        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveisPorOrgao(any(Long.class)))
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao
                                .builder()
                                .id(1L)
                                .build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.JULY.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.JULY.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .id(9L)
                            .build())
                        .dataAtivacao(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.JANUARY.getValue()))
                        .numero("0000000001")
                        .build()));

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        Mockito.when(configAmortizacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
                .sistema("Intangivel")
                .contaContabil(ContaContabil.builder().id(1L).build())
                .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                .vidaUtil((short) 60)
                .situacao(ConfigAmortizacao.Situacao.ATIVO)
                .taxa(new BigDecimal("1.666666666667").setScale(12, RoundingMode.HALF_EVEN))
                .percentualResidual(BigDecimal.valueOf(0))
                .build()));

        IniciaAmortizacaoManualUseCaseImpl usecase = new IniciaAmortizacaoManualUseCaseImpl(patrimonioDataProvider,
            configAmortizacaoDataProvider,
            dadosAmortizacaoDataProvider,
            salvaDadosAmortizacaoUseCase,
            amortizacaoDataProvider,
            fixedClock,
            new IniciaAmortizacaoManualOutputDataConverter());

        IniciaAmortizacaoManualOutputData outputData = usecase.executar(new IniciaAmortizacaoManualInputData((long) 9, "04", "2020"));

        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(1, outputData.getItems().size());
        Assert.assertEquals("0000000001", outputData.getItems().get(0).getNumero());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
    }

    @Test
    public void deveAmortizarPatrimonioNaoAcumulado() {
        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveisPorOrgao(any(Long.class)))
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao
                                .builder()
                                .id(1L)
                                .build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.JULY.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.JULY.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .id(9L)
                            .build())
                        .dataAtivacao(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.JANUARY.getValue()))
                        .numero("0000000001")
                        .build()));

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        Mockito.when(configAmortizacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
                .sistema("Intangivel")
                .contaContabil(ContaContabil.builder().id(1L).build())
                .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                .vidaUtil((short) 60)
                .situacao(ConfigAmortizacao.Situacao.ATIVO)
                .taxa(new BigDecimal("1.666666666667").setScale(12, RoundingMode.HALF_EVEN))
                .percentualResidual(BigDecimal.valueOf(0))
                .build()));

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(amortizacaoDataProvider.buscarUltimaPorPatrimonio(any(Long.class)))
            .thenReturn(Optional.of(Amortizacao
                .builder()
                .dataInicial(LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(1).withYear(2020))
                .dataFinal(LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(31).withYear(2020))
                .build()));

        IniciaAmortizacaoManualUseCaseImpl usecase = new IniciaAmortizacaoManualUseCaseImpl(patrimonioDataProvider,
            configAmortizacaoDataProvider,
            dadosAmortizacaoDataProvider,
            salvaDadosAmortizacaoUseCase,
            amortizacaoDataProvider,
            fixedClock,
            new IniciaAmortizacaoManualOutputDataConverter());

        IniciaAmortizacaoManualInputData inputData = new IniciaAmortizacaoManualInputData();
        inputData.setOrgao(9L);
        inputData.setMes("04");
        inputData.setAno("2020");

        IniciaAmortizacaoManualOutputData outputData = usecase.executar(inputData);

        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(1, outputData.getItems().size());
        Assert.assertEquals("0000000001", outputData.getItems().get(0).getNumero());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
    }

    @Test
    public void deveAmortizarPatrimonioVencido() {
        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveisPorOrgao(any(Long.class)))
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .amortizavel(Boolean.TRUE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao
                                .builder()
                                .id(1L)
                                .build())
                            .build())
                        .dataVencimento(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.MARCH.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.MARCH.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .id(9L)
                            .build())
                        .dataAtivacao(LocalDateTime
                            .now()
                            .withYear(2020)
                            .withDayOfMonth(7)
                            .withMonth(Month.JANUARY.getValue()))
                        .numero("0000000001")
                        .build()));

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        Mockito.when(configAmortizacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
                .sistema("Intangivel")
                .contaContabil(ContaContabil.builder().id(1L).build())
                .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                .vidaUtil((short) 60)
                .situacao(ConfigAmortizacao.Situacao.ATIVO)
                .taxa(new BigDecimal("1.666666666667").setScale(12, RoundingMode.HALF_EVEN))
                .percentualResidual(BigDecimal.valueOf(0))
                .build()));

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(any(Long.class)))
            .thenReturn(Boolean.FALSE);


        IniciaAmortizacaoManualUseCaseImpl usecase = new IniciaAmortizacaoManualUseCaseImpl(patrimonioDataProvider,
            configAmortizacaoDataProvider,
            dadosAmortizacaoDataProvider,
            salvaDadosAmortizacaoUseCase,
            amortizacaoDataProvider,
            fixedClock,
            new IniciaAmortizacaoManualOutputDataConverter());

        IniciaAmortizacaoManualOutputData outputData = IniciaAmortizacaoManualOutputData
            .builder()
            .items(usecase.executar(IniciaAmortizacaoManualInputData.builder().ano("2020").mes("04").orgao(9L).build()).getItems())
            .build();

        Assert.assertFalse(outputData.getItems().isEmpty());
        Assert.assertEquals(1, outputData.getItems().size());
        Assert.assertEquals("0000000001", outputData.getItems().get(0).getNumero());
        Assert.assertEquals(java.util.Optional.of(1L).get(), outputData.getItems().get(0).getId());
    }
}
