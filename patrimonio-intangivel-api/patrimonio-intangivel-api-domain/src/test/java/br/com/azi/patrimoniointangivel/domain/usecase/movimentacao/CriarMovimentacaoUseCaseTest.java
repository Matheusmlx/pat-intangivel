package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.converter.CriarMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoUseCase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class CriarMovimentacaoUseCaseTest {

    @Test
    public void deveCriarUmaMovimentacao(){
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        GerarCodigoDeMovimentacaoUseCase gerarCodigoDeMovimentacaoUseCase = Mockito.mock(GerarCodigoDeMovimentacaoUseCase.class);

        CriarMovimentacaoUseCaseImpl useCase = new CriarMovimentacaoUseCaseImpl(
            patrimonioDataProvider,
            movimentacaoDataProvider,
            new CriarMovimentacaoOutputDataConverter(),
            gerarCodigoDeMovimentacaoUseCase
        );

        CriarMovimentacaoInputData inputData = CriarMovimentacaoInputData
            .builder()
            .idPatrimonio(1L)
            .tipo("DOACAO_ENTRE_ORGAOS")
            .build();

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(gerarCodigoDeMovimentacaoUseCase.executar())
            .thenReturn(GerarCodigoDeMovimentacaoOutputData
                .builder()
                .codigo("00001")
                .build());

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio
                .builder()
                .id(1L)
                .valorLiquido(BigDecimal.valueOf(1500))
                .orgao(UnidadeOrganizacional .builder() .id(1L) .build())
                .build()));

        Mockito.when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao
                .builder()
                .id(1L)
                .codigo("00001")
                .tipo(Movimentacao.Tipo.valueOf("DOACAO_ENTRE_ORGAOS"))
                .orgaoOrigem(UnidadeOrganizacional .builder() .id(1L) .build())
                .patrimonio(Patrimonio.builder() .id(1L) .build())
                .build());

        CriarMovimentacaoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1),outputData.getId());
        Assert.assertEquals("00001",outputData.getCodigo());
    }
    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoEnviarIdDoPatrimonio(){
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        GerarCodigoDeMovimentacaoUseCase gerarCodigoDeMovimentacaoUseCase = Mockito.mock(GerarCodigoDeMovimentacaoUseCase.class);


        CriarMovimentacaoUseCaseImpl useCase = new CriarMovimentacaoUseCaseImpl(
            patrimonioDataProvider,
            movimentacaoDataProvider,
            new CriarMovimentacaoOutputDataConverter(),
            gerarCodigoDeMovimentacaoUseCase
        );

        CriarMovimentacaoInputData inputData = CriarMovimentacaoInputData
            .builder()
            .tipo("DOACAO_ENTRE_ORGAOS")
            .build();
        useCase.executar(inputData);
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarPatrimonio(){
        MovimentacaoDataProvider movimentacaoDataProvider = Mockito.mock(MovimentacaoDataProvider.class);
        PatrimonioDataProvider patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        GerarCodigoDeMovimentacaoUseCase gerarCodigoDeMovimentacaoUseCase = Mockito.mock(GerarCodigoDeMovimentacaoUseCase.class);

        CriarMovimentacaoUseCaseImpl useCase = new CriarMovimentacaoUseCaseImpl(
            patrimonioDataProvider,
            movimentacaoDataProvider,
            new CriarMovimentacaoOutputDataConverter(),
            gerarCodigoDeMovimentacaoUseCase
        );

        CriarMovimentacaoInputData inputData = CriarMovimentacaoInputData
            .builder()
            .idPatrimonio(1L)
            .tipo("DOACAO_ENTRE_ORGAOS")
            .build();
        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        useCase.executar(inputData);
    }
}
