package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao;


import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.AtivarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.AtivarPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.AtivarPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.converter.AtivarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception.AtivarPatrimonioException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception.DataAtivacaoIgualDataDeVencimentoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioUseCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AtivarPatrimonioUseCaseTest {

    Short vidaUtilDefinida;
    SalvaConfigAmortizacaoUseCase salvaConfigAmortizacaoUseCase;
    DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Before
    public void inicializa() {
        vidaUtilDefinida = 36;
        salvaConfigAmortizacaoUseCase = Mockito.mock(SalvaConfigAmortizacaoUseCase.class);
        dadosAmortizacaoDataProvider = Mockito.mock(DadosAmortizacaoDataProvider.class);
        lancamentosContabeisDataProvider = Mockito.mock(LancamentosContabeisDataProvider.class);
    }

    @Test
    public void deveAtivarPatrimonio() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase = Mockito.mock(GerarNumeroPatrimonioUseCase.class);
        AtivarPatrimonioInputData inputData = new AtivarPatrimonioInputData(1L);

        AtivarPatrimonioUseCaseImpl useCase = new AtivarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new AtivarPatrimonioOutputDataConverter(),
            (short) 36,
            gerarNumeroPatrimonioUseCase,
            salvaConfigAmortizacaoUseCase,
            dadosAmortizacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            "01/01/2020");

        Mockito.when(gerarNumeroPatrimonioUseCase.executar())
            .thenReturn(GerarNumeroPatrimonioOutputData
                .builder()
                .numero("0000000001")
                .build());

        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ContaContabil
                .builder()
                .id(1L)
                .build()));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .tipo(ConfigContaContabil.Tipo.AMORTIZAVEL)
                .build()));

        Mockito.when(salvaConfigAmortizacaoUseCase.executar(any(SalvaConfigAmortizacaoInputData.class)))
            .thenReturn(SalvaConfigAmortizacaoOutputData
                .builder()
                .id(1L)
                .metodo("QUOTAS_CONSTANTES")
                .vidaUtil((short) 36)
                .situacao("ATIVO")
                .taxa(BigDecimal.valueOf(100))
                .percentualResidual(BigDecimal.valueOf(0))
                .build());

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.TRANSACAO_SEM_CONTRAPRESTACAO)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .dataAtivacao(LocalDateTime.of(2020,Month.AUGUST,1,0,0))
                .dataVencimento(LocalDateTime.of(2020,Month.AUGUST,28,0,0))
                .vidaIndefinida(Boolean.FALSE)
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
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .numero("0000000001")
                .build()));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .valorLiquido(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
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
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build());

        AtivarPatrimonioOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("0000000001", outputData.getNumero());
    }

    @Test
    public void deveAtivarPatrimonioAquisicaoSeparada() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase = Mockito.mock(GerarNumeroPatrimonioUseCase.class);
        AtivarPatrimonioInputData inputData = new AtivarPatrimonioInputData(1L);

        AtivarPatrimonioUseCaseImpl useCase = new AtivarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new AtivarPatrimonioOutputDataConverter(),
            (short) 36,
            gerarNumeroPatrimonioUseCase,
            salvaConfigAmortizacaoUseCase,
            dadosAmortizacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            "01/01/2020");

        Mockito.when(gerarNumeroPatrimonioUseCase.executar())
            .thenReturn(GerarNumeroPatrimonioOutputData
                .builder()
                .numero("0000000001")
                .build());

        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ContaContabil
                .builder()
                .id(1L)
                .build()));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .tipo(ConfigContaContabil.Tipo.AMORTIZAVEL)
                .build()));

        Mockito.when(salvaConfigAmortizacaoUseCase.executar(any(SalvaConfigAmortizacaoInputData.class)))
            .thenReturn(SalvaConfigAmortizacaoOutputData
                .builder()
                .id(1L)
                .metodo("QUOTAS_CONSTANTES")
                .vidaUtil((short) 36)
                .situacao("ATIVO")
                .taxa(BigDecimal.valueOf(100))
                .percentualResidual(BigDecimal.valueOf(0))
                .build());

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
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
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0))
                .dataVencimento(LocalDateTime.of(2050, Month.JANUARY, 1, 0, 0))
                .numero("0000000001")
                .build()));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .valorLiquido(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
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
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build());

        AtivarPatrimonioOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("0000000001", outputData.getNumero());
    }
    @Test(expected = DataAtivacaoIgualDataDeVencimentoException.class)
    public void deveFalharQuandoDataDeVencimentoForIgualADataDeAtivacao(){
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase = Mockito.mock(GerarNumeroPatrimonioUseCase.class);
        AtivarPatrimonioInputData inputData = new AtivarPatrimonioInputData(1L);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)
                .descricao("Software desenvolvido internamente sendo ativado")
                .amortizavel(Boolean.TRUE)
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
                .dataAtivacao(LocalDateTime.of(2020,Month.AUGUST,8,0,0,0))
                .dataVencimento(LocalDateTime.of(2020,Month.AUGUST,8,23,0,0))
                .orgao(UnidadeOrganizacional.builder().sigla("AGEHAB").build())
                .setor(UnidadeOrganizacional.builder().sigla("setor a").build())
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .numero("0000000001")
                .build()));

        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ContaContabil
                .builder()
                .id(1L)
                .build()));

        Mockito.when(gerarNumeroPatrimonioUseCase.executar())
            .thenReturn(GerarNumeroPatrimonioOutputData
                .builder()
                .numero("0000000001")
                .build());

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .tipo(ConfigContaContabil.Tipo.AMORTIZAVEL)
                .build()));

        AtivarPatrimonioUseCaseImpl useCase = new AtivarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new AtivarPatrimonioOutputDataConverter(),
            (short) 36,
            gerarNumeroPatrimonioUseCase,
            salvaConfigAmortizacaoUseCase,
            dadosAmortizacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            "01/01/2020");

        useCase.executar(inputData);
    }


    @Test
    public void deveCalcularVidaUtilAPartirDaDataDeVencimento() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase = Mockito.mock(GerarNumeroPatrimonioUseCase.class);
        AtivarPatrimonioInputData inputData = new AtivarPatrimonioInputData(1L);

        AtivarPatrimonioUseCaseImpl useCase = new AtivarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new AtivarPatrimonioOutputDataConverter(),
            (short) 36,
            gerarNumeroPatrimonioUseCase,
            salvaConfigAmortizacaoUseCase,
            dadosAmortizacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            "01/01/2020");

        Mockito.when(gerarNumeroPatrimonioUseCase.executar())
            .thenReturn(GerarNumeroPatrimonioOutputData
                .builder()
                .numero("0000000001")
                .build());


        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ContaContabil
                .builder()
                .id(1L)
                .build()));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .tipo(ConfigContaContabil.Tipo.AMORTIZAVEL)
                .build()));

        Mockito.when(salvaConfigAmortizacaoUseCase.executar(any(SalvaConfigAmortizacaoInputData.class)))
            .thenReturn(SalvaConfigAmortizacaoOutputData
                .builder()
                .id(1L)
                .metodo("QUOTAS_CONSTANTES")
                .vidaUtil((short) 36)
                .situacao("ATIVO")
                .taxa(BigDecimal.valueOf(100))
                .percentualResidual(BigDecimal.valueOf(0))
                .build());


        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.GERACAO_INTERNA)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
                .dataAtivacao(LocalDateTime.of(2020, 1, 1, 0, 0,0))
                .dataVencimento(LocalDateTime.of(2025, 10, 1, 0, 0,0))
                .orgao(UnidadeOrganizacional.builder().sigla("AGEHAB").build())
                .setor(UnidadeOrganizacional.builder().sigla("setor a").build())
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build()));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .valorLiquido(BigDecimal.valueOf(10000.00))
                .orgao(UnidadeOrganizacional.builder().sigla("AGEHAB").build())
                .setor(UnidadeOrganizacional.builder().sigla("setor a").build())
                .vidaIndefinida(Boolean.FALSE)
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build());

        useCase.executar(inputData);
        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
    }

    @Test
    public void deveSetarDataAtivacaoQuandoNaoTiver() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase = Mockito.mock(GerarNumeroPatrimonioUseCase.class);
        AtivarPatrimonioInputData inputData = new AtivarPatrimonioInputData(1L);

        AtivarPatrimonioUseCaseImpl useCase = new AtivarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new AtivarPatrimonioOutputDataConverter(),
            (short) 36,
            gerarNumeroPatrimonioUseCase,
            salvaConfigAmortizacaoUseCase,
            dadosAmortizacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            "01/01/2020");

        Mockito.when(gerarNumeroPatrimonioUseCase.executar())
            .thenReturn(GerarNumeroPatrimonioOutputData
                .builder()
                .numero("0000000001")
                .build());


        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ContaContabil
                .builder()
                .id(1L)
                .build()));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .tipo(ConfigContaContabil.Tipo.AMORTIZAVEL)
                .build()));

        Mockito.when(salvaConfigAmortizacaoUseCase.executar(any(SalvaConfigAmortizacaoInputData.class)))
            .thenReturn(SalvaConfigAmortizacaoOutputData
                .builder()
                .id(1L)
                .metodo("QUOTAS_CONSTANTES")
                .vidaUtil((short) 36)
                .situacao("ATIVO")
                .taxa(BigDecimal.valueOf(100))
                .percentualResidual(BigDecimal.valueOf(0))
                .build());


        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.GERACAO_INTERNA)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
                .dataVencimento(LocalDateTime.of(2100, 10, 10, 10, 0,  0,  0))
                .orgao(UnidadeOrganizacional.builder().sigla("AGEHAB").build())
                .setor(UnidadeOrganizacional.builder().sigla("setor a").build())
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .numero("0000000001")
                .build()));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .valorLiquido(BigDecimal.valueOf(10000.00))
                .orgao(UnidadeOrganizacional.builder().sigla("AGEHAB").build())
                .setor(UnidadeOrganizacional.builder().sigla("setor a").build())
                .vidaIndefinida(Boolean.FALSE)
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build());

        useCase.executar(inputData);
        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
    }

    @Test(expected = AtivarPatrimonioException.class)
    public void deveFalharQuandoDataAtivacaoAnteriorAJaneiro() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase = Mockito.mock(GerarNumeroPatrimonioUseCase.class);
        AtivarPatrimonioInputData inputData = new AtivarPatrimonioInputData(1L);

        AtivarPatrimonioUseCaseImpl useCase = new AtivarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new AtivarPatrimonioOutputDataConverter(),
            (short) 36,
            gerarNumeroPatrimonioUseCase,
            salvaConfigAmortizacaoUseCase,
            dadosAmortizacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            "01/01/2020");

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.GERACAO_INTERNA)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
                .dataVencimento(LocalDateTime
                    .now()
                    .withDayOfMonth(7)
                    .withMonth(Month.SEPTEMBER.getValue()))
                .orgao(UnidadeOrganizacional.builder().sigla("AGEHAB").build())
                .setor(UnidadeOrganizacional.builder().sigla("setor a").build())
                .contaContabil(ContaContabil
                    .builder()
                    .id(1L)
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .numero("0000000001")
                .dataAtivacao(LocalDateTime.now() .withMonth(10) .withYear(2015) .withDayOfMonth(3))
                .build()));


        useCase.executar(inputData);
        Mockito.verify(patrimonioDataProvider, Mockito.times(1)).buscarPorId(any(Long.class));
    }

    @Test
    public void deveAtivarPatrimonioNaoAmortizavel() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        ConfigContaContabilDataProvider configContaContabilDataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase = Mockito.mock(GerarNumeroPatrimonioUseCase.class);
        AtivarPatrimonioInputData inputData = new AtivarPatrimonioInputData(1L);

        AtivarPatrimonioUseCaseImpl useCase = new AtivarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new AtivarPatrimonioOutputDataConverter(),
            (short) 36,
            gerarNumeroPatrimonioUseCase,
            salvaConfigAmortizacaoUseCase,
            dadosAmortizacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            "01/01/2020");

        Mockito.when(gerarNumeroPatrimonioUseCase.executar())
            .thenReturn(GerarNumeroPatrimonioOutputData
                .builder()
                .numero("0000000001")
                .build());

        Mockito.when(contaContabilDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(ContaContabil
                .builder()
                .id(1L)
                .build()));

        Mockito.when(configContaContabilDataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(ConfigContaContabil
                .builder()
                .id(1L)
                .tipo(ConfigContaContabil.Tipo.NAO_AMORTIZAVEL)
                .build()));

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .reconhecimento(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
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
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .dataVencimento(LocalDateTime.of(2050, Month.JANUARY, 1, 0, 0))
                .numero("0000000001")
                .build()));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.ATIVO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .descricao("Software desenvolvido internamente sendo ativado")
                .valorAquisicao(BigDecimal.valueOf(10000.00))
                .valorLiquido(BigDecimal.valueOf(10000.00))
                .vidaIndefinida(Boolean.FALSE)
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
                    .codigo("124110200")
                    .descricao("Conta contábil para softwares em desenvolvimento")
                    .build())
                .dataAtivacao(LocalDateTime.now())
                .numero("0000000001")
                .build());

        AtivarPatrimonioOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("0000000001", outputData.getNumero());
    }
}
