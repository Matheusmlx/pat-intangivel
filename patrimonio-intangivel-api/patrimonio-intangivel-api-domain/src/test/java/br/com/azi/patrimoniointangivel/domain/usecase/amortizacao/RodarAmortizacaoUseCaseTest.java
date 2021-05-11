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
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.RodarAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.exception.RodarAmortizacaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoUseCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RodarAmortizacaoUseCaseTest {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 5, 1);
    private Clock fixedClock;

    @Before
    public void start() {
        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    @Test
    public void deveRodarAmortizacaoPrimeiroMes() throws ParseException {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase = Mockito.mock(SalvaDadosAmortizacaoUseCase.class);
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);

        RodarAmortizacaoUseCaseImpl usecase = new RodarAmortizacaoUseCaseImpl(patrimonioDataProvider,
            salvaDadosAmortizacaoUseCase, amortizacaoDataProvider,
            configAmortizacaoDataProvider, dadosAmortizacaoDataProvider, fixedClock);

        LocalDateTime dataInicio = LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(11).withYear(2020);
        LocalDateTime dataFim = LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(31).withYear(2020);

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveis())
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .descricao("Software desenvolvido internamente sendo ativado")
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
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
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .sigla("AGEHAB")
                            .build())
                        .setor(UnidadeOrganizacional
                            .builder()
                            .sigla("setor a")
                            .build())
                        .contaContabil(ContaContabil
                            .builder()
                            .id(1L)
                            .codigo("124110100")
                            .descricao("Conta contábil para softwares")
                            .build())
                        .dataAtivacao(LocalDateTime.now())
                        .numero("0000000001")
                        .build()));

        SalvaDadosAmortizacaoInputData inputData = new SalvaDadosAmortizacaoInputData(
            any(Long.class),
            dataInicio,
            dataFim);

        salvaDadosAmortizacaoUseCase.executar(inputData);

        usecase.executar();
        Mockito.verify(salvaDadosAmortizacaoUseCase, Mockito.times(1)).executar(inputData);
    }

    @Test(expected = RodarAmortizacaoException.class)
    public void deveFalharQuandoNaoTiverConfigAmortizacao() throws ParseException {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase = Mockito.mock(SalvaDadosAmortizacaoUseCase.class);
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);

        RodarAmortizacaoUseCaseImpl usecase = new RodarAmortizacaoUseCaseImpl(patrimonioDataProvider,
            salvaDadosAmortizacaoUseCase, amortizacaoDataProvider,
            configAmortizacaoDataProvider, dadosAmortizacaoDataProvider, fixedClock);

        LocalDateTime dataInicio = LocalDateTime.now().withDayOfMonth(11).withMonth(Month.MARCH.getValue()).withYear(2020);
        LocalDateTime dataFim = LocalDateTime.now().withDayOfMonth(21).withMonth(Month.MARCH.getValue()).withYear(2020);

        Mockito.when(configAmortizacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveis())
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .descricao("Software desenvolvido internamente sendo ativado")
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
                        .dadosAmortizacao(DadosAmortizacao
                            .builder()
                            .id(1L)
                            .configAmortizacao(ConfigAmortizacao
                                .builder()
                                .id(1L)
                                .build())
                            .build())
                        .amortizavel(Boolean.TRUE)
                        .dataVencimento(LocalDateTime
                            .now()
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .sigla("AGEHAB")
                            .build())
                        .setor(UnidadeOrganizacional
                            .builder()
                            .sigla("setor a")
                            .build())
                        .contaContabil(ContaContabil
                            .builder()
                            .id(1L)
                            .codigo("124110100")
                            .descricao("Conta contábil para softwares")
                            .build())
                        .dataAtivacao(LocalDateTime.now().withYear(2020).withMonth(Month.FEBRUARY.getValue()).withDayOfMonth(2))
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

        SalvaDadosAmortizacaoInputData inputData = new SalvaDadosAmortizacaoInputData(
            any(Long.class),
            dataInicio,
            dataFim);

        salvaDadosAmortizacaoUseCase.executar(inputData);

        usecase.executar();
        Mockito.verify(salvaDadosAmortizacaoUseCase, Mockito.times(1)).executar(inputData);
    }

    @Test
    public void deveRodarAmortizacao() throws ParseException {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase = Mockito.mock(SalvaDadosAmortizacaoUseCase.class);
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);

        RodarAmortizacaoUseCaseImpl usecase = new RodarAmortizacaoUseCaseImpl(patrimonioDataProvider,
            salvaDadosAmortizacaoUseCase, amortizacaoDataProvider,
            configAmortizacaoDataProvider, dadosAmortizacaoDataProvider, fixedClock);

        LocalDateTime dataInicio = LocalDateTime.now().withDayOfMonth(11).withMonth(Month.MARCH.getValue()).withYear(2020);
        LocalDateTime dataFim = LocalDateTime.now().withDayOfMonth(21).withMonth(Month.MARCH.getValue()).withYear(2020);

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

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveis())
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .descricao("Software desenvolvido internamente sendo amortizado")
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
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
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .sigla("AGEHAB")
                            .build())
                        .setor(UnidadeOrganizacional
                            .builder()
                            .sigla("setor a")
                            .build())
                        .contaContabil(ContaContabil
                            .builder()
                            .id(1L)
                            .codigo("124110100")
                            .descricao("Conta contábil para softwares")
                            .build())
                        .dataAtivacao(LocalDateTime
                            .now()
                            .withYear(2019)
                            .withDayOfMonth(2)
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

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(amortizacaoDataProvider.buscarUltimaPorPatrimonio(any(Long.class)))
            .thenReturn(Optional.of(Amortizacao
                .builder()
                .configAmortizacao(ConfigAmortizacao.builder().build())
                .dataInicial(LocalDateTime
                    .now()
                    .withYear(2019)
                    .withDayOfMonth(2)
                    .withMonth(Month.JANUARY.getValue()))
                .dataFinal(LocalDateTime
                    .now()
                    .withYear(2020)
                    .withDayOfMonth(25)
                    .withMonth(Month.JANUARY.getValue()))
                .valorAnterior(BigDecimal.valueOf(1000))
                .valorPosterior(BigDecimal.valueOf(9500))
                .valorSubtraido(BigDecimal.valueOf(500))
                .build()));

        SalvaDadosAmortizacaoInputData inputData = new SalvaDadosAmortizacaoInputData(
            any(Long.class),
            dataInicio,
            dataFim);

        salvaDadosAmortizacaoUseCase.executar(inputData);

        usecase.executar();
        Mockito.verify(salvaDadosAmortizacaoUseCase, Mockito.times(1)).executar(inputData);
    }

    @Test
    public void deveCriarRegistrosSeparadosAmortizacaoAcumulada() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase = Mockito.mock(SalvaDadosAmortizacaoUseCase.class);
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);

        RodarAmortizacaoUseCaseImpl usecase = new RodarAmortizacaoUseCaseImpl(patrimonioDataProvider,
            salvaDadosAmortizacaoUseCase, amortizacaoDataProvider,
            configAmortizacaoDataProvider, dadosAmortizacaoDataProvider, fixedClock);

        LocalDateTime dataInicio = LocalDateTime.now().withMonth(Month.APRIL.getValue()).withDayOfMonth(1).withYear(2020);
        LocalDateTime dataFim = LocalDateTime.now().withMonth(Month.APRIL.getValue()).withDayOfMonth(30).withYear(2020);

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

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveis())
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .descricao("Software desenvolvido internamente sendo amortizado")
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
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
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .sigla("AGEHAB")
                            .build())
                        .setor(UnidadeOrganizacional
                            .builder()
                            .sigla("setor a")
                            .build())
                        .contaContabil(ContaContabil
                            .builder()
                            .id(1L)
                            .codigo("124110100")
                            .descricao("Conta contábil para softwares")
                            .build())
                        .dataAtivacao(LocalDateTime
                            .now()
                            .withYear(2019)
                            .withDayOfMonth(12)
                            .withMonth(Month.DECEMBER.getValue()))
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

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        SalvaDadosAmortizacaoInputData inputData = new SalvaDadosAmortizacaoInputData(
            any(Long.class),
            dataInicio,
            dataFim);

        salvaDadosAmortizacaoUseCase.executar(inputData);

        usecase.executar();
        Mockito.verify(salvaDadosAmortizacaoUseCase, Mockito.times(1)).executar(inputData);
    }

    @Test
    public void deveCriarRegistroAmortizacaoNaoAcumulada() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase = Mockito.mock(SalvaDadosAmortizacaoUseCase.class);
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);

        RodarAmortizacaoUseCaseImpl usecase = new RodarAmortizacaoUseCaseImpl(patrimonioDataProvider,
            salvaDadosAmortizacaoUseCase, amortizacaoDataProvider,
            configAmortizacaoDataProvider, dadosAmortizacaoDataProvider, fixedClock);

        LocalDateTime dataInicio = LocalDateTime.now().withMonth(Month.APRIL.getValue()).withDayOfMonth(1).withYear(2020);
        LocalDateTime dataFim = LocalDateTime.now().withMonth(Month.APRIL.getValue()).withDayOfMonth(30).withYear(2020);

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

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveis())
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .descricao("Software desenvolvido internamente sendo amortizado")
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
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
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .fimVidaUtil(LocalDateTime
                            .now()
                            .withDayOfMonth(7)
                            .withMonth(Month.SEPTEMBER.getValue()))
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .sigla("AGEHAB")
                            .build())
                        .setor(UnidadeOrganizacional
                            .builder()
                            .sigla("setor a")
                            .build())
                        .contaContabil(ContaContabil
                            .builder()
                            .id(1L)
                            .codigo("124110100")
                            .descricao("Conta contábil para softwares")
                            .build())
                        .dataAtivacao(LocalDateTime
                            .now()
                            .withYear(2019)
                            .withDayOfMonth(12)
                            .withMonth(Month.DECEMBER.getValue()))
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

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(amortizacaoDataProvider.buscarUltimaPorPatrimonio(any(Long.class)))
            .thenReturn(Optional.of(Amortizacao
                .builder()
                .dataInicial(LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(1).withYear(2020))
                .dataFinal(LocalDateTime.now().withMonth(Month.MARCH.getValue()).withDayOfMonth(31).withYear(2020))
                .build()));

        SalvaDadosAmortizacaoInputData inputData = new SalvaDadosAmortizacaoInputData(
            any(Long.class),
            dataInicio,
            dataFim);

        salvaDadosAmortizacaoUseCase.executar(inputData);

        usecase.executar();
        Mockito.verify(salvaDadosAmortizacaoUseCase, Mockito.times(1)).executar(inputData);
    }

    @Test
    public void deveCriarRegistrosSeparadosAmortizacaoUnicaVencido() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase = Mockito.mock(SalvaDadosAmortizacaoUseCase.class);
        ConfigAmortizacaoDataProvider configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        AmortizacaoDataProvider amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);

        RodarAmortizacaoUseCaseImpl usecase = new RodarAmortizacaoUseCaseImpl(patrimonioDataProvider,
            salvaDadosAmortizacaoUseCase, amortizacaoDataProvider,
            configAmortizacaoDataProvider, dadosAmortizacaoDataProvider, fixedClock);

        LocalDateTime dataInicio = LocalDateTime.now().withMonth(Month.APRIL.getValue()).withDayOfMonth(1).withYear(2020);
        LocalDateTime dataFim = LocalDateTime.now().withMonth(Month.APRIL.getValue()).withDayOfMonth(30).withYear(2020);

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

        Mockito.when(patrimonioDataProvider.buscarPatrimoniosAmortizaveis())
            .thenReturn(
                List.of(
                    Patrimonio.builder()
                        .id(1L)
                        .nome("Sistema Interno")
                        .tipo(Patrimonio.Tipo.SOFTWARES)
                        .situacao(Patrimonio.Situacao.ATIVO)
                        .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                        .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                        .descricao("Software desenvolvido internamente sendo amortizado")
                        .valorAquisicao(BigDecimal.valueOf(10000.00))
                        .vidaIndefinida(Boolean.FALSE)
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
                            .sigla("AGEHAB")
                            .build())
                        .setor(UnidadeOrganizacional
                            .builder()
                            .sigla("setor a")
                            .build())
                        .contaContabil(ContaContabil
                            .builder()
                            .id(1L)
                            .codigo("124110100")
                            .descricao("Conta contábil para softwares")
                            .build())
                        .dataAtivacao(LocalDateTime
                            .now()
                            .withYear(2019)
                            .withDayOfMonth(12)
                            .withMonth(Month.DECEMBER.getValue()))
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

        Mockito.when(amortizacaoDataProvider.existePorPatrimonio(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        SalvaDadosAmortizacaoInputData inputData = new SalvaDadosAmortizacaoInputData(
            any(Long.class),
            dataInicio,
            dataFim);

        salvaDadosAmortizacaoUseCase.executar(inputData);

        usecase.executar();
        Mockito.verify(salvaDadosAmortizacaoUseCase, Mockito.times(1)).executar(inputData);
    }
}
