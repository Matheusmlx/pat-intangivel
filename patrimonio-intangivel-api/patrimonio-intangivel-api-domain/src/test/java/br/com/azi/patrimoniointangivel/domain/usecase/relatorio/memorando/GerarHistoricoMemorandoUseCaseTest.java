package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.HistoricoMemorando;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.HistoricoMemorandoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.GerarHistoricoMemorandoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.GerarHistoricoMemorandoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.exception.FalhaEmUploadException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GerarHistoricoMemorandoUseCaseTest {

    private UploadTemporarioUseCase uploadTemporarioUseCase;

    private HistoricoMemorandoDataProvider historicoMemorandoDataProvider;

    @Before
    public void start() {
        uploadTemporarioUseCase = Mockito.mock(UploadTemporarioUseCase.class);
        historicoMemorandoDataProvider = Mockito.mock(HistoricoMemorandoDataProvider.class);
    }

    @Test
    public void deveRealizarUploadESalvarHistorico() {
        GerarHistoricoMemorandoUseCaseImpl useCase = new GerarHistoricoMemorandoUseCaseImpl(
            uploadTemporarioUseCase,
            historicoMemorandoDataProvider);

        Arquivo arquivo = Arquivo
            .builder()
            .nome("arquivo.pdf")
            .contentType("application/pdf")
            .content(new byte[]{})
            .build();

        Mockito.when(uploadTemporarioUseCase.executar(any(UploadTemporarioInputData.class)))
            .thenReturn(
                UploadTemporarioOutputData
                    .builder()
                    .nome("arquivo.pdf")
                    .contentType("application/pdf")
                    .uri("uri")
                    .url("url")
                    .build());

        GerarHistoricoMemorandoInputData inputData = GerarHistoricoMemorandoInputData
            .builder()
            .numeroMemorando("000001/2020")
            .arquivo(arquivo)
            .build();

        useCase.executar(inputData);

        Mockito.verify(uploadTemporarioUseCase, Mockito.times(1)).executar(any(UploadTemporarioInputData.class));
        Mockito.verify(historicoMemorandoDataProvider, Mockito.times(1)).salvar(any(HistoricoMemorando.class));
    }

    @Test(expected = FalhaEmUploadException.class)
    public void deveRealizarUploadESalvarHistorico1() {
        GerarHistoricoMemorandoUseCaseImpl useCase = new GerarHistoricoMemorandoUseCaseImpl(
            uploadTemporarioUseCase,
            historicoMemorandoDataProvider);

        Arquivo arquivo = Arquivo
            .builder()
            .nome("arquivo.pdf")
            .contentType("application/pdf")
            .content(new byte[]{})
            .build();

        GerarHistoricoMemorandoInputData inputData = GerarHistoricoMemorandoInputData
            .builder()
            .numeroMemorando("000001/2020")
            .arquivo(arquivo)
            .build();

        useCase.executar(inputData);

        Mockito.verify(uploadTemporarioUseCase, Mockito.times(1)).executar(any(UploadTemporarioInputData.class));
        Mockito.verify(historicoMemorandoDataProvider, Mockito.times(1)).salvar(any(HistoricoMemorando.class));
    }
}
