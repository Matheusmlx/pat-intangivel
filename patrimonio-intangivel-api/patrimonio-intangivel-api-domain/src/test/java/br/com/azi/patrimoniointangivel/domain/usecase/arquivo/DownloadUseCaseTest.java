package br.com.azi.patrimoniointangivel.domain.usecase.arquivo;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.DownloadInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.DownloadOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.DownloadUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.converter.DownloadOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class DownloadUseCaseTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharSemCamposObrigatorios() {

        DownloadInputData inputData = new DownloadInputData();
        DownloadUseCaseImpl useCase = new
            DownloadUseCaseImpl(
            Mockito.mock(SistemaDeArquivosIntegration.class),
            Mockito.mock(DownloadOutputDataConverter.class));

        useCase.executar(inputData);
    }

    @Test
    public void deveRetornarOArquivoBuscado() {
        DownloadInputData inputData = DownloadInputData
            .builder()
            .uri("uri")
            .build();

        SistemaDeArquivosIntegration sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);
        DownloadOutputDataConverter downloadOutputDataConverter = Mockito.mock(DownloadOutputDataConverter.class);
        DownloadUseCaseImpl useCase = new
            DownloadUseCaseImpl(
            sistemaDeArquivosIntegration,
            downloadOutputDataConverter);

        Mockito.when(sistemaDeArquivosIntegration.getMetadata(any(String.class)))
            .thenReturn(Arquivo.builder().build());

        Mockito.when(sistemaDeArquivosIntegration.download(any(String.class)))
            .thenReturn(Arquivo.builder().build());

        Mockito.when(downloadOutputDataConverter.to(any(Arquivo.class)))
            .thenReturn(DownloadOutputData.builder().build());

        DownloadOutputData outputData = useCase.executar(inputData);

        Assert.assertNotNull(outputData);

        Mockito.verify(sistemaDeArquivosIntegration, Mockito.times(1)).getMetadata(any(String.class));
        Mockito.verify(sistemaDeArquivosIntegration, Mockito.times(1)).download(any(String.class));
        Mockito.verify(downloadOutputDataConverter, Mockito.times(1)).to(any(Arquivo.class));
    }
}
