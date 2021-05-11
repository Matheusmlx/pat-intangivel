package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.conveter.EditarMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.exception.OrgaoDestinoIgualOrgaoOrigemException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarMovimentacaoUseCaseTest {

    @InjectMocks
    EditarMovimentacaoUseCaseImpl useCase;

    @Mock
    NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Mock
    MovimentacaoDataProvider movimentacaoDataProvider;

    @InjectMocks
    EditarMovimentacaoOutputDataConverter outputDataConverter;

    @Before
    public void gerarInstanciaDoUseCase(){
        useCase = new EditarMovimentacaoUseCaseImpl(movimentacaoDataProvider,notaLancamentoContabilDataProvider,outputDataConverter);
    }


    @Test
    public void deveEditarMovimentacao() {
        EditarMovimentacaoInputData inputData = EditarMovimentacaoInputData
            .builder()
            .id(1L)
            .patrimonio(1L)
            .motivo("Patrimonio não esta sendo mais utilizado")
            .numeroNL("5555NL555555")
            .dataNL(new Date().from(LocalDateTime.now().withYear(2020).withMonth(Month.JANUARY.getValue()).withDayOfMonth(2).atZone(ZoneId.systemDefault()).toInstant()))
            .orgaoDestino(2L)
            .build();

        when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimonio esta sendo utilizados")
                .notaLancamentoContabil(NotaLancamentoContabil.builder().numero("5555NL555555").build())
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .build()));

        when(movimentacaoDataProvider.salvar(any(Movimentacao.class)))
            .thenReturn(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimonio não esta sendo mais utilizado")
                .orgaoOrigem(UnidadeOrganizacional.builder().id(1L).build())
                .notaLancamentoContabil(NotaLancamentoContabil.builder().numero("5555NL555555").build())
                .orgaoDestino(UnidadeOrganizacional.builder().id(2L).build())
                .build());

        EditarMovimentacaoOutputData outputData = useCase.executar(inputData);
        assertEquals(Long.valueOf(1),outputData.getId());
        assertEquals("Patrimonio não esta sendo mais utilizado",outputData.getMotivo());
        assertEquals(Long.valueOf(2),outputData.getOrgaoDestino());
        assertEquals("5555NL555555",outputData.getNumeroNL());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarId() {
        EditarMovimentacaoInputData inputData = EditarMovimentacaoInputData
            .builder()
            .motivo("Cadeira não está sendo mais utilizada")
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoExistir() {
        EditarMovimentacaoInputData inputData = EditarMovimentacaoInputData
            .builder()
            .id(1L)
            .motivo("Cadeira não está sendo mais utilizada")
            .build();

        when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.FALSE);

        useCase.executar(inputData);
    }

    @Test(expected = OrgaoDestinoIgualOrgaoOrigemException.class)
    public void deveFalharQuandoOrgaoDestinoForIgualAoOrgaoOrigem() {
        EditarMovimentacaoInputData inputData = EditarMovimentacaoInputData
            .builder()
            .id(1L)
            .motivo("Cadeira não está sendo mais utilizada")
            .orgaoDestino(129L)
            .build();

        when(movimentacaoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder()
                .id(1L)
                .motivo("Patrimonio não esta sendo mais utilizado")
                .orgaoOrigem(UnidadeOrganizacional .builder() .id(129L) .build())
                .build()));

        useCase.executar(inputData);
    }
}
