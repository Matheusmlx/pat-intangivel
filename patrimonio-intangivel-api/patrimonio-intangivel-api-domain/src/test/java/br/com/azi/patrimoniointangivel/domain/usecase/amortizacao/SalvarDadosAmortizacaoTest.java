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
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.calculaquotasconstantes.QuotasConstantesUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.exception.AmortizacaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.exception.QuotasContantesValidacaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SalvarDadosAmortizacaoTest {

    PatrimonioDataProvider patrimonioDataProvider;
    ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;
    DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;
    AmortizacaoDataProvider amortizacaoDataProvider;
    QuotasConstantesUseCase quotasConstantesUseCase;
    LocalDateTime dataIncio;
    LocalDateTime dataFim;
    SalvaDadosAmortizacaoInputData inputData;

    @Before
    public void incializa() throws ParseException {
        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        configAmortizacaoDataProvider = Mockito.mock(ConfigAmortizacaoDataProvider.class);
        dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);
        amortizacaoDataProvider = Mockito.mock(AmortizacaoDataProvider.class);
        quotasConstantesUseCase = Mockito.mock(QuotasConstantesUseCase.class);
        dataIncio = LocalDateTime.now().withDayOfMonth(11).withMonth(Month.MARCH.getValue()).withYear(2020);
        dataFim = LocalDateTime.now().withDayOfMonth(11).withMonth(Month.MARCH.getValue()).withYear(2025);
        inputData = new SalvaDadosAmortizacaoInputData(1L, dataIncio, dataFim);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarIdPatrimonio() {

        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        usecase.executar(SalvaDadosAmortizacaoInputData.builder().build());
    }

    @Test(expected = QuotasContantesValidacaoException.class)
    public void deveFalharQuandoNaoTiverConfigAmortizacao() {

        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido calculando amortizacao")
                .valorAquisicao(BigDecimal.valueOf(30000.00))
                .vidaIndefinida(Boolean.FALSE)
                .valorLiquido(BigDecimal.valueOf(30000.00))
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .fimVidaUtil(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(dadosAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(configAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        usecase.executar(inputData);

        Mockito.verify(amortizacaoDataProvider, Mockito.times(1)).salvar(any(Amortizacao.class));
    }

    @Test(expected = AmortizacaoException.class)
    public void deveFalharQuandoNaoBuscarConfigAmortizacao() {
        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido calculando amortizacao")
                .valorAquisicao(BigDecimal.valueOf(30000.00))
                .vidaIndefinida(Boolean.FALSE)
                .valorLiquido(BigDecimal.valueOf(30000.00))
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .fimVidaUtil(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(dadosAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(configAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        usecase.executar(inputData);

        Mockito.verify(amortizacaoDataProvider, Mockito.times(1)).salvar(any(Amortizacao.class));
    }

    @Test(expected = QuotasContantesValidacaoException.class)
    public void deveFalharQuandoConfigAmortizacaoInativo() {
        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        Mockito.when(configAmortizacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ConfigAmortizacao
                .builder()
                .id(1L)
                .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL)
                .sistema("Intangivel")
                .contaContabil(ContaContabil.builder().id(1L).build())
                .metodo(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES)
                .vidaUtil((short) 60)
                .situacao(ConfigAmortizacao.Situacao.INATIVO)
                .taxa(new BigDecimal("1.666666666667").setScale(12, RoundingMode.HALF_EVEN))
                .percentualResidual(BigDecimal.valueOf(0))
                .build()));

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido calculando amortizacao")
                .valorAquisicao(BigDecimal.valueOf(30000.00))
                .vidaIndefinida(Boolean.FALSE)
                .valorLiquido(BigDecimal.valueOf(30000.00))
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .fimVidaUtil(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));


        Mockito.when(dadosAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(configAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        usecase.executar(inputData);

        Mockito.verify(amortizacaoDataProvider, Mockito.times(1)).salvar(any(Amortizacao.class));
    }

    @Test(expected = QuotasContantesValidacaoException.class)
    public void deveFalharQuandoNaoTiverVinculoDadosAmortizacao() {

        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido calculando amortizacao")
                .valorAquisicao(BigDecimal.valueOf(30000.00))
                .vidaIndefinida(Boolean.FALSE)
                .valorLiquido(BigDecimal.valueOf(30000.00))
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .fimVidaUtil(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(dadosAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        usecase.executar(inputData);

        Mockito.verify(amortizacaoDataProvider, Mockito.times(1)).salvar(any(Amortizacao.class));
    }

    @Test(expected = QuotasContantesValidacaoException.class)
    public void deveFalharQuandoConfiguracaoAmortizacaoForNula() {

        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido calculando amortizacao")
                .valorAquisicao(BigDecimal.valueOf(30000.00))
                .vidaIndefinida(Boolean.FALSE)
                .valorLiquido(BigDecimal.valueOf(30000.00))
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .fimVidaUtil(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(dadosAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .build()
        ));

        usecase.executar(inputData);

        Mockito.verify(amortizacaoDataProvider, Mockito.times(1)).salvar(any(Amortizacao.class));
    }

    @Test
    public void deveAtualizarValorLiquidoPatrimonio() {

        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        Mockito.when(quotasConstantesUseCase.executar(any(QuotasConstantesInputData.class)))
            .thenReturn(QuotasConstantesOutputData
                .builder()
                .valorAnterior(new BigDecimal(30000).setScale(12, RoundingMode.HALF_EVEN))
                .valorPosteiror(new BigDecimal(29500).setScale(12, RoundingMode.HALF_EVEN))
                .valorSubtraido(new BigDecimal(500).setScale(12, RoundingMode.HALF_EVEN))
                .build());

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

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido calculando amortizacao")
                .valorAquisicao(BigDecimal.valueOf(30000.00))
                .vidaIndefinida(Boolean.FALSE)
                .valorLiquido(BigDecimal.valueOf(30000.00))
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .fimVidaUtil(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(dadosAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(configAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        usecase.executar(inputData);

        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).atualizar(any(Patrimonio.class));
    }

    @Test
    public void deveSalvarAmortizacao() {

        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        Mockito.when(quotasConstantesUseCase.executar(any(QuotasConstantesInputData.class)))
            .thenReturn(QuotasConstantesOutputData
                .builder()
                .valorAnterior(new BigDecimal(30000).setScale(12, RoundingMode.HALF_EVEN))
                .valorPosteiror(new BigDecimal(29500).setScale(12, RoundingMode.HALF_EVEN))
                .valorSubtraido(new BigDecimal(500).setScale(12, RoundingMode.HALF_EVEN))
                .build());

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

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido calculando amortizacao")
                .valorAquisicao(BigDecimal.valueOf(30000.00))
                .vidaIndefinida(Boolean.FALSE)
                .valorLiquido(BigDecimal.valueOf(30000.00))
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .fimVidaUtil(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(dadosAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(configAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        usecase.executar(inputData);

        Mockito.verify(amortizacaoDataProvider, Mockito.times(1)).salvar(any(Amortizacao.class));
    }

    @Test
    public void deveSalvarAmortizacaoUltimoMesVidaUtil() {

        SalvaDadosAmortizacaoUseCaseImpl usecase = new SalvaDadosAmortizacaoUseCaseImpl(patrimonioDataProvider, dadosAmortizacaoDataProvider,
            configAmortizacaoDataProvider, amortizacaoDataProvider, quotasConstantesUseCase);

        Mockito.when(quotasConstantesUseCase.executar(any(QuotasConstantesInputData.class)))
            .thenReturn(QuotasConstantesOutputData
                .builder()
                .valorAnterior(new BigDecimal(30000).setScale(12, RoundingMode.HALF_EVEN))
                .valorPosteiror(new BigDecimal(29500).setScale(12, RoundingMode.HALF_EVEN))
                .valorSubtraido(new BigDecimal(500).setScale(12, RoundingMode.HALF_EVEN))
                .build());

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

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido calculando amortizacao")
                .valorAquisicao(BigDecimal.valueOf(30000.00))
                .vidaIndefinida(Boolean.FALSE)
                .valorLiquido(BigDecimal.valueOf(30000.00))
                .dadosAmortizacao(DadosAmortizacao
                    .builder()
                    .id(1L)
                    .configAmortizacao(ConfigAmortizacao
                        .builder()
                        .id(1L)
                        .build())
                    .build())
                .fimVidaUtil(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional
                    .builder()
                    .sigla("AGEHAB")
                    .build()
                )
                .setor(UnidadeOrganizacional
                    .builder()
                    .sigla("setor a")
                    .build()
                )
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110100")
                    .descricao("Conta contábil para softwares")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(dadosAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(configAmortizacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(dadosAmortizacaoDataProvider.buscarPorId(any(Long.class))).thenReturn(Optional.of(
            DadosAmortizacao
                .builder()
                .id(1L)
                .patrimonio(Patrimonio.builder().id(1L).build())
                .configAmortizacao(ConfigAmortizacao.builder().id(1L).build())
                .build()
        ));

        SalvaDadosAmortizacaoInputData inputData = new SalvaDadosAmortizacaoInputData();
        inputData.setDataFinal(LocalDateTime.of(2020, Month.SEPTEMBER, 30, 23, 59, 59));
        inputData.setDataInicio(LocalDateTime.of(2020, Month.SEPTEMBER, 1, 0, 0, 0));
        inputData.setPatrimonio(1L);
        usecase.executar(inputData);

        Mockito.verify(amortizacaoDataProvider, Mockito.times(1)).salvar(any(Amortizacao.class));
    }
}
