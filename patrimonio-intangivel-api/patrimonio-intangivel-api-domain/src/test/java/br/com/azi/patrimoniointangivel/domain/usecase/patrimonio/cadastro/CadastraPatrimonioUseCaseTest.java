package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro;

import br.com.azi.patrimoniointangivel.domain.entity.CodigoContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.converter.CadastraPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.exception.EscolheTipoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class CadastraPatrimonioUseCaseTest {

    @Test(expected = EscolheTipoException.class)
    public void deveFalharTipoBemDiferentedoEnum() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase = Mockito.mock(GerarNumeroMemorandoUseCase.class);

        CadastraPatrimonioUseCaseImpl useCase = new CadastraPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new CadastraPatrimonioOutputDataConverter(),
            contaContabilDataProvider,
            gerarNumeroMemorandoUseCase,
            codigoContaContabil);

        CadastraPatrimonioInputData inputData = CadastraPatrimonioInputData
            .builder()
            .tipo("CAFETEIRA")
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveSalvarTipoBem() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase = Mockito.mock(GerarNumeroMemorandoUseCase.class);


        CadastraPatrimonioUseCaseImpl useCase = new CadastraPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new CadastraPatrimonioOutputDataConverter(),
            contaContabilDataProvider,
            gerarNumeroMemorandoUseCase,
            codigoContaContabil
        );

        CadastraPatrimonioInputData inputData = CadastraPatrimonioInputData
            .builder()
            .tipo("SOFTWARES")
            .build();

        Mockito.when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        Mockito.when(patrimonioDataProvider.salvar(any(Patrimonio.class)))
            .thenReturn(Patrimonio
                .builder()
                .id(1L)
                .tipo(Patrimonio.Tipo.SOFTWARES)
                .nome("Office")
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build());

        CadastraPatrimonioOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("SOFTWARES", outputData.getTipo());
        Assert.assertEquals("Office", outputData.getNome());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
    }

    @Test
    public void deveSalvarPatrimonioMarca() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase = Mockito.mock(GerarNumeroMemorandoUseCase.class);


        CadastraPatrimonioUseCaseImpl useCase = new CadastraPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new CadastraPatrimonioOutputDataConverter(),
            contaContabilDataProvider,
            gerarNumeroMemorandoUseCase,
            codigoContaContabil
        );

        CadastraPatrimonioInputData inputData = CadastraPatrimonioInputData
            .builder()
            .tipo("MARCAS")
            .build();

        Mockito.when(contaContabilDataProvider.buscarPorCodigo(any()))
            .thenReturn(Optional.of(
                ContaContabil
                    .builder()
                    .id(4L)
                    .codigo("124210100")
                    .descricao("MARCAS E PATENTES INDUSTRIAIS")
                    .build()
            ));

        Mockito.when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        Mockito.when(patrimonioDataProvider.salvar(any(Patrimonio.class)))
            .thenReturn(Patrimonio
                .builder()
                .id(1L)
                .tipo(Patrimonio.Tipo.MARCAS)
                .nome("Office")
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build());

        CadastraPatrimonioOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("MARCAS", outputData.getTipo());
        Assert.assertEquals("Office", outputData.getNome());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
    }

    @Test
    public void deveSalvarPatrimonioReceita() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase = Mockito.mock(GerarNumeroMemorandoUseCase.class);


        CadastraPatrimonioUseCaseImpl useCase = new CadastraPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new CadastraPatrimonioOutputDataConverter(),
            contaContabilDataProvider,
            gerarNumeroMemorandoUseCase,
            codigoContaContabil
        );

        CadastraPatrimonioInputData inputData = CadastraPatrimonioInputData
            .builder()
            .tipo("RECEITAS_FORMULAS_PROJETOS")
            .build();

        Mockito.when(contaContabilDataProvider.buscarPorCodigo(any()))
            .thenReturn(Optional.of(
                ContaContabil
                    .builder()
                    .id(4L)
                    .codigo("124210100")
                    .descricao("RECEITAS FORMULAS E PROJETOS")
                    .build()
            ));

        Mockito.when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        Mockito.when(patrimonioDataProvider.salvar(any(Patrimonio.class)))
            .thenReturn(Patrimonio
                .builder()
                .id(1L)
                .tipo(Patrimonio.Tipo.RECEITAS_FORMULAS_PROJETOS)
                .nome("Office")
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build());

        CadastraPatrimonioOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("RECEITAS_FORMULAS_PROJETOS", outputData.getTipo());
        Assert.assertEquals("Office", outputData.getNome());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
    }

    @Test
    public void deveSalvarPatrimonioDireitoAutoral() {
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        ContaContabilDataProvider contaContabilDataProvider = Mockito.mock(ContaContabilDataProvider.class);
        CodigoContaContabil codigoContaContabil = Mockito.mock(CodigoContaContabil.class);
        GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase = Mockito.mock(GerarNumeroMemorandoUseCase.class);


        CadastraPatrimonioUseCaseImpl useCase = new CadastraPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new CadastraPatrimonioOutputDataConverter(),
            contaContabilDataProvider,
            gerarNumeroMemorandoUseCase,
            codigoContaContabil
        );

        CadastraPatrimonioInputData inputData = CadastraPatrimonioInputData
            .builder()
            .tipo("DIREITOS_AUTORAIS")
            .build();


        Mockito.when(contaContabilDataProvider.buscarPorCodigo(any()))
            .thenReturn(Optional.of(
                ContaContabil
                    .builder()
                    .id(3L)
                    .codigo("124210300")
                    .descricao("DIREITOS AUTORAIS")
                    .build()
            ));

        Mockito.when(gerarNumeroMemorandoUseCase.executar()).thenReturn(GerarNumeroMemorandoOutputData.builder().numeroMemorando("000001").build());

        Mockito.when(patrimonioDataProvider.salvar(any(Patrimonio.class)))
            .thenReturn(Patrimonio
                .builder()
                .id(1L)
                .tipo(Patrimonio.Tipo.DIREITOS_AUTORAIS)
                .nome("Office")
                .situacao(Patrimonio.Situacao.EM_ELABORACAO)
                .build());

        CadastraPatrimonioOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("DIREITOS_AUTORAIS", outputData.getTipo());
        Assert.assertEquals("Office", outputData.getNome());
        Assert.assertEquals("EM_ELABORACAO", outputData.getSituacao());
    }
}
