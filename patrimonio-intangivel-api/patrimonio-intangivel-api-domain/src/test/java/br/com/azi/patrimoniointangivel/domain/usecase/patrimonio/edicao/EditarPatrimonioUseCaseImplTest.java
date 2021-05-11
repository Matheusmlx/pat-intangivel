package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao;

import br.com.azi.patrimoniointangivel.domain.entity.CodigoContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.converter.EditarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception.DataAtivacaoMenorQueDataAquisicaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception.PatrimonioNomeInvalidoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarPatrimonioUseCaseImplTest  {

    @InjectMocks
    private EditarPatrimonioUseCaseImpl editarPatrimonioUseCase;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private EditarPatrimonioOutputDataConverter outputDataConverter;

    @Mock
    private ContaContabilDataProvider contaContabilDataProvider;

    @Mock
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Mock
    private CodigoContaContabil codigoContaContabil;

    private EditarPatrimonioInputData inputData;

    private Patrimonio patrimonio = Patrimonio.builder().build();

    private final Date dataMaior = Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(2).atZone(ZoneId.systemDefault()).toInstant());
    private final Date dataMenor = Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(1).atZone(ZoneId.systemDefault()).toInstant());

    private void buildPatrimonio(Patrimonio.Tipo tipo) {
        patrimonio = Patrimonio.builder().tipo(tipo).situacao(Patrimonio.Situacao.EM_ELABORACAO).build();
    }

    private void buildEditarPatrimonioInputData(Integer id, String nome, String tipo, Date dataAquisicao, Date dataAtivacao) {
        inputData = EditarPatrimonioInputData
            .builder()
            .id(id != null ? (long) id : null)
            .nome(nome)
            .tipo(tipo)
            .dataAtivacao(dataAtivacao)
            .build();

        if (Optional.ofNullable(dataAquisicao).isPresent()) {
            inputData.setDataAquisicao(dataAquisicao);
        }
    }

    @Before
    public void setUp() {
        Whitebox.setInternalState(editarPatrimonioUseCase, "patrimonioDataProvider", patrimonioDataProvider);
        Whitebox.setInternalState(editarPatrimonioUseCase, "outputDataConverter", outputDataConverter);
        Whitebox.setInternalState(editarPatrimonioUseCase, "contaContabilDataProvider", contaContabilDataProvider);
        Whitebox.setInternalState(editarPatrimonioUseCase, "codigoContaContabil", codigoContaContabil);
    }

    @Test(expected = DataAtivacaoMenorQueDataAquisicaoException.class)
    public void deveRetornarExceptionQuandoDataDeAtivacaoForMenorQueDataDeAquisicao() {
        buildPatrimonio(Patrimonio.Tipo.LICENCAS);
        when(patrimonioDataProvider.existe(Mockito.anyLong())).thenReturn(true);
        when(patrimonioDataProvider.buscarPorId(Mockito.anyLong())).thenReturn(Optional.of(patrimonio));
        buildEditarPatrimonioInputData(
            1,
            "Teste",
            "LICENCAS",
            dataMaior,
            dataMenor
        );
        buildPatrimonio(Patrimonio.Tipo.LICENCAS);
        editarPatrimonioUseCase.executar(inputData);
    }

    @Test
    public void deveEditarPatrimonio() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .nome("Alura")
            .tipo("SOFTWARES")
            .situacao("EM_ELABORACAO")
            .estado("PRONTO_PARA_USO")
            .dataAtivacao(dataMaior)
            .dataAquisicao(dataMaior)
            .ativacaoRetroativa(Boolean.TRUE)
            .dataNL(dataMaior)
            .dataVencimento(new Date().from(LocalDateTime.now().withYear(2031).withMonth(Month.JANUARY.getValue()).withDayOfMonth(2).atZone(ZoneId.systemDefault()).toInstant()))
            .descricao("Descricao do sofware")
            .fornecedor(1L)
            .numeroNL("5555NL555555")
            .dataNL(new Date().from(LocalDateTime.now().withYear(2020).withMonth(Month.MARCH.getValue()).withDayOfMonth(2).atZone(ZoneId.systemDefault()).toInstant()))
            .orgao(1L)
            .setor(1L)
            .reconhecimento("AQUISICAO_SEPARADA")
            .valorAquisicao(null)
            .vidaIndefinida(Boolean.FALSE)
            .build();

        EditarPatrimonioOutputDataConverter atualizarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();

        EditarPatrimonioUseCaseImpl useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            atualizarPatrimonioOutputDataConverter,
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider

        );
        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("Alura")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .valorAquisicao(BigDecimal.valueOf(100))
                .build()));

        when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        when(notaLancamentoContabilDataProvider.salvar(any(NotaLancamentoContabil.class)))
            .thenReturn(NotaLancamentoContabil
                .builder()
                .id(1L)
                .numero("5555NL555555")
                .dataLancamento(LocalDateTime.of(2020,Month.MARCH,2,0,0))
                .build());

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Alura")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .valorAquisicao(null)
                .notaLancamentoContabil(NotaLancamentoContabil
                    .builder()
                    .numero("5555NL555555")
                    .dataLancamento(LocalDateTime.of(2020,Month.MARCH,2,0,0))
                    .build())
                .contaContabil(ContaContabil
                    .builder()
                    .id(2L)
                    .codigo("124110200")
                    .descricao("BENS INTANGIVEIS>SOFTWARE EM DESENVOLVIMENTO")
                    .build())
                .build());

        when(contaContabilDataProvider.buscarPorCodigo(codigoContaContabil.getSoftwareDesenvolvimento()))
            .thenReturn( Optional.of(ContaContabil
                .builder()
                .id(2L)
                .codigo("124110200")
                .descricao("BENS INTANGIVEIS>SOFTWARE EM DESENVOLVIMENTO")
                .build()));

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);
        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("SOFTWARES", outputData.getTipo());
        assertEquals("Alura", outputData.getNome());
        assertEquals("5555NL555555",outputData.getNumeroNL());
        assertEquals("2020-03-02T00:00:00.000-0400",outputData.getDataNL());


    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);

        buildEditarPatrimonioInputData(
            null,
            "Teste",
            "LICENCAS",
            dataMaior,
            dataMaior
        );

        EditarPatrimonioUseCaseImpl usecase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            Mockito.mock(EditarPatrimonioOutputDataConverter.class),
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider);

        usecase.executar(inputData);
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharPatrimonioNaoExistente() {
        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(50L)
            .nome("Alura")
            .tipo("SOFTWARES")
            .estado("EM_DESENVOLVIMENTO")
            .dataAtivacao(dataMaior)
            .dataAquisicao(dataMaior)
            .build();

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);

        EditarPatrimonioOutputDataConverter atualizarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();
        EditarPatrimonioUseCaseImpl useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            atualizarPatrimonioOutputDataConverter,
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider);

        when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);
        useCase.executar(inputData);
    }

    @Test(expected = PatrimonioNomeInvalidoException.class)
    public void deveFalharQuandoNomeMaiorQue100Caracteres() {
        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .nome("Alura alura alura alura não é software, intellij é software. aliás esse é um campo para nome não para dissertar sobre a vida")
            .tipo("SOFTWARES")
            .estado("EM_DESENVOLVIMENTO")
            .dataAtivacao(dataMaior)
            .dataAquisicao(dataMaior)
            .build();

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);

        EditarPatrimonioOutputDataConverter atualizarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();
        EditarPatrimonioUseCaseImpl useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            atualizarPatrimonioOutputDataConverter,
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider);
        useCase.executar(inputData);
    }

    @Test
    public void deveRemoverEspacodoNomedoPatrimonio() {
        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .nome("  Intellij  ")
            .tipo("SOFTWARES")
            .situacao("EM_ELABORACAO")
            .valorAquisicao(BigDecimal.valueOf(100))
            .dataAtivacao(dataMaior)
            .dataAquisicao(dataMaior)
            .build();
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);

        EditarPatrimonioOutputDataConverter atualizarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();
        EditarPatrimonioUseCaseImpl useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            atualizarPatrimonioOutputDataConverter,
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider);
        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("IDEA")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build()));

        when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Intellij")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .valorAquisicao(BigDecimal.valueOf(100))
                .contaContabil(ContaContabil
                    .builder()
                    .id(2L)
                    .codigo("124110200")
                    .descricao("BENS INTANGIVEIS>SOFTWARE EM DESENVOLVIMENTO")
                    .build())
                .build());

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);
        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("SOFTWARES", outputData.getTipo());
        assertEquals("Intellij", outputData.getNome());
    }

    @Test
    public void deveSetarContaContabilParaSoftwareEmDesenvolvimento() {
        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .nome("Sistema Interno")
            .tipo("SOFTWARES")
            .situacao("EM_ELABORACAO")
            .estado("EM_DESENVOLVIMENTO")
            .dataAtivacao(dataMaior)
            .dataAquisicao(dataMaior)
            .build();

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        EditarPatrimonioOutputDataConverter atualizarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();

        EditarPatrimonioUseCaseImpl useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            atualizarPatrimonioOutputDataConverter,
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider);
        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("IDEA")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build()));

        when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("Sistema Interno")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.EM_DESENVOLVIMENTO)
                .contaContabil(ContaContabil
                    .builder()
                    .id(2L)
                    .codigo("124110200")
                    .descricao("BENS INTANGIVEIS>SOFTWARE EM DESENVOLVIMENTO")
                    .build())
                .build());

        when(contaContabilDataProvider.buscarPorCodigo(any()))
            .thenReturn(Optional.of(
                ContaContabil
                    .builder()
                    .id(2L)
                    .codigo("124110200")
                    .descricao("BENS INTANGIVEIS>SOFTWARE EM DESENVOLVIMENTO")
                    .build()
            ));

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("SOFTWARES", outputData.getTipo());
        assertEquals("Sistema Interno", outputData.getNome());
        assertEquals(Long.valueOf(2), outputData.getContaContabil().getId());
        assertEquals("124110200", outputData.getContaContabil().getCodigo());
        assertEquals("BENS INTANGIVEIS>SOFTWARE EM DESENVOLVIMENTO", outputData.getContaContabil().getDescricao());
    }

    @Test
    public void deveAtualizarContaContabilParaDireitos() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        EditarPatrimonioOutputDataConverter atualizarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .nome("IDEA")
            .tipo("DIREITOS_AUTORAIS")
            .situacao("EM_ELABORACAO")
            .estado("EM_DESENVOLVIMENTO")
            .dataAtivacao(dataMaior)
            .dataAquisicao(dataMaior)
            .build();

        EditarPatrimonioUseCaseImpl useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            atualizarPatrimonioOutputDataConverter,
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider);

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("IDEA")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .contaContabil(ContaContabil
                    .builder()
                    .id(2L)
                    .codigo("124110200")
                    .descricao("BENS INTANGIVEIS")
                    .build())
                .build()));

        when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("IDEA")
                .tipo(Patrimonio.Tipo.DIREITOS_AUTORAIS)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .contaContabil(ContaContabil
                    .builder()
                    .id(3L)
                    .codigo("124210300")
                    .descricao("DIREITOS AUTORAIS")
                    .build())
                .build());

        when(contaContabilDataProvider.buscarPorCodigo(any()))
            .thenReturn(Optional.of(
                ContaContabil
                    .builder()
                    .id(3L)
                    .codigo("124210300")
                    .descricao("DIREITOS AUTORAIS")
                    .build()
            ));

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("DIREITOS_AUTORAIS", outputData.getTipo());
        assertEquals("IDEA", outputData.getNome());
        assertEquals(Long.valueOf(3), outputData.getContaContabil().getId());
        assertEquals("124210300", outputData.getContaContabil().getCodigo());
        assertEquals("DIREITOS AUTORAIS", outputData.getContaContabil().getDescricao());
    }

    @Test
    public void deveAtualizarContaContabilParaMarcas() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        EditarPatrimonioOutputDataConverter atualizarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .nome("IDEA")
            .tipo("MARCAS")
            .situacao("EM_ELABORACAO")
            .estado("EM_DESENVOLVIMENTO")
            .dataAtivacao(dataMaior)
            .dataAquisicao(dataMaior)
            .build();

        EditarPatrimonioUseCaseImpl useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            atualizarPatrimonioOutputDataConverter,
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider);

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("IDEA")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .contaContabil(ContaContabil
                    .builder()
                    .id(2L)
                    .codigo("124110200")
                    .descricao("BENS INTANGIVEIS")
                    .build())
                .build()));

        when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("IDEA")
                .tipo(Patrimonio.Tipo.MARCAS)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .contaContabil(ContaContabil
                    .builder()
                    .id(4L)
                    .codigo("124210100")
                    .descricao("MARCAS E PATENTES INDUSTRIAIS")
                    .build())
                .build());

        when(contaContabilDataProvider.buscarPorCodigo(any()))
            .thenReturn(Optional.of(
                ContaContabil
                    .builder()
                    .id(4L)
                    .codigo("124210100")
                    .descricao("MARCAS E PATENTES INDUSTRIAIS")
                    .build()
            ));

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("MARCAS", outputData.getTipo());
        assertEquals("IDEA", outputData.getNome());
        assertEquals(Long.valueOf(4), outputData.getContaContabil().getId());
        assertEquals("124210100", outputData.getContaContabil().getCodigo());
        assertEquals("MARCAS E PATENTES INDUSTRIAIS", outputData.getContaContabil().getDescricao());
    }

    @Test
    public void deveAtualizarContaContabilParaReceitas() {

        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        EditarPatrimonioOutputDataConverter atualizarPatrimonioOutputDataConverter = new EditarPatrimonioOutputDataConverter();

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .nome("IDEA")
            .tipo("RECEITAS_FORMULAS_PROJETOS")
            .situacao("EM_ELABORACAO")
            .estado("EM_DESENVOLVIMENTO")
            .dataAtivacao(dataMaior)
            .dataAquisicao(dataMaior)
            .build();

        EditarPatrimonioUseCaseImpl useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            atualizarPatrimonioOutputDataConverter,
            contaContabilDataProvider,
            codigoContaContabil,
            notaLancamentoContabilDataProvider);

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder()
                .id(1L)
                .nome("IDEA")
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .contaContabil(ContaContabil
                    .builder()
                    .id(2L)
                    .codigo("124110200")
                    .descricao("BENS INTANGIVEIS")
                    .build())
                .build()));

        when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(Patrimonio.builder()
                .id(1L)
                .nome("IDEA")
                .tipo(Patrimonio.Tipo.RECEITAS_FORMULAS_PROJETOS)
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .estado(Patrimonio.Estado.PRONTO_PARA_USO)
                .contaContabil(ContaContabil
                    .builder()
                    .id(4L)
                    .codigo("124210100")
                    .descricao("RECEITAS FORMULAS E PROJETOS")
                    .build())
                .build());

        when(contaContabilDataProvider.buscarPorCodigo(any()))
            .thenReturn(Optional.of(
                ContaContabil
                    .builder()
                    .id(4L)
                    .codigo("124210100")
                    .descricao("RECEITAS FORMULAS E PROJETOS")
                    .build()
            ));

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("RECEITAS_FORMULAS_PROJETOS", outputData.getTipo());
        assertEquals("IDEA", outputData.getNome());
        assertEquals(Long.valueOf(4), outputData.getContaContabil().getId());
        assertEquals("124210100", outputData.getContaContabil().getCodigo());
        assertEquals("RECEITAS FORMULAS E PROJETOS", outputData.getContaContabil().getDescricao());
    }

}
