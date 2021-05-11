package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.exception.TipoDocumentoNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.converter.CriarDocumentoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.expection.NumeroUnicoException;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.expection.QuantidadeDeDocumentosCadastradoExcedidoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
public class CriarDocumentoMovimentacaoUseCaseTest {

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @InjectMocks
    private CriarDocumentoOutputDataConverter outputDataConverter;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @InjectMocks
    private CriarDocumentoMovimentacaoUseCaseImpl useCase;

    @Before
    public void gerarInstanciaDoUseCase(){
        useCase = new CriarDocumentoMovimentacaoUseCaseImpl(movimentacaoDataProvider,tipoDocumentoDataprovider,documentoDataProvider,sistemaDeArquivosIntegration,patrimonioDataProvider,outputDataConverter);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoCadastrarSemNada() {
        useCase.executar(new CriarDocumentoMovimentacaoInputData());
    }

    @Test
    public void deveSalvarDocumento() {
        CriarDocumentoMovimentacaoInputData inputData = CriarDocumentoMovimentacaoInputData
            .builder()
            .numero("00045")
            .valor(BigDecimal.valueOf(451))
            .data(new Date().from(LocalDateTime.now().withYear(2020).withMonth(Month.JANUARY.getValue()).withDayOfMonth(2).atZone(ZoneId.systemDefault()).toInstant()))
            .idPatrimonio(1L)
            .idTipoDocumento(2L)
            .idMovimentacao(1L)
            .build();


        when(documentoDataProvider.existeDocumentoMovimentacaoComNumero(inputData.getIdMovimentacao(),inputData.getNumero(),inputData.getIdTipoDocumento()))
            .thenReturn(false);

       when(documentoDataProvider.salvar(any(Documento.class)))
           .thenReturn(Documento
               .builder()
               .id(1L)
               .numero("6")
               .patrimonio(Patrimonio.builder().id(1L).build())
               .movimentacao(Movimentacao.builder().id(1L).build())
               .tipoDocumento(TipoDocumento.builder().id(2L).build())
               .data(LocalDateTime.parse("2019-09-09T10:15:30"))
               .valor(BigDecimal.valueOf(450))
               .uriAnexo("documento.pdf")
               .build());


       when(tipoDocumentoDataprovider.buscarPorId(any(Long.class)))
         .thenReturn(Optional.of(
             TipoDocumento
                 .builder()
                 .id(1L)
                 .build()));

       when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
           .thenReturn(Optional.of(
               Movimentacao
                   .builder()
                   .id(1L)
                   .build()));

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .build()));

        CriarDocumentoMovimentacaoOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1),outputData.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharCasoIdPatrimonioForNulo(){
        useCase.executar(CriarDocumentoMovimentacaoInputData.builder().idMovimentacao(1L).idTipoDocumento(2L).build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharCasoIdMovimentacaoForNulo(){
        useCase.executar(CriarDocumentoMovimentacaoInputData.builder().idPatrimonio(1L).idTipoDocumento(2L).build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharCasoIdTipoDocumentoForNulo(){
        useCase.executar(CriarDocumentoMovimentacaoInputData.builder().idPatrimonio(1L).idMovimentacao(2L).build());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharCasoMovimentacaoNaoEncontrada(){
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(CriarDocumentoMovimentacaoInputData.builder().idPatrimonio(1L).idMovimentacao(2L).idTipoDocumento(1L).build());
    }

    @Test(expected = PatrimonioNaoEncontradoException.class)
    public void deveFalharCasoPatrimonioNaoEncontrado(){
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder().id(2L).build()));

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(CriarDocumentoMovimentacaoInputData.builder().idPatrimonio(1L).idMovimentacao(2L).idTipoDocumento(1L).build());
    }

    @Test(expected = TipoDocumentoNaoEncontradoException.class)
    public void deveFalharCasoTipoDocumentoNaoSejaEncontrado(){
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder().id(2L).build()));

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder().id(1L).build()));

        when(tipoDocumentoDataprovider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());
        useCase.executar(CriarDocumentoMovimentacaoInputData.builder().idPatrimonio(1L).idMovimentacao(2L).idTipoDocumento(1L).build());
    }

    @Test(expected = QuantidadeDeDocumentosCadastradoExcedidoException.class)
    public void deveFalharCasoQuantidadeDeDocumentosPorMovimentacaoSejaMaiorQue30(){
        when(movimentacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Movimentacao.builder().id(2L).build()));

        when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Patrimonio.builder().id(1L).build()));

        when(tipoDocumentoDataprovider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(TipoDocumento.builder().id(1L).build()));

        when(documentoDataProvider.qntDocumentosPorMovimentacaoId(any(Long.class)))
            .thenReturn(35L);

        useCase.executar(CriarDocumentoMovimentacaoInputData.builder().idPatrimonio(1L).idMovimentacao(2L).idTipoDocumento(1L).build());
    }

    @Test(expected = NumeroUnicoException.class)
    public void deveFalharCasoNumeroDeDocumentoJaEstejaEmUso(){
        CriarDocumentoMovimentacaoInputData inputData = CriarDocumentoMovimentacaoInputData
            .builder()
            .idTipoDocumento(1L)
            .idMovimentacao(2L)
            .idPatrimonio(3L)
            .build();

        when(documentoDataProvider.existeDocumentoMovimentacaoComNumero(inputData.getIdMovimentacao(),inputData.getNumero(),inputData.getIdTipoDocumento()))
            .thenReturn(true);

        useCase.executar(inputData);
    }
}
