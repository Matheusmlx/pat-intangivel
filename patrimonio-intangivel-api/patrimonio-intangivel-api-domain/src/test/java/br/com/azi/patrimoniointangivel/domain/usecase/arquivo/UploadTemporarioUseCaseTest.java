package br.com.azi.patrimoniointangivel.domain.usecase.arquivo;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.UploadTemporarioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.upload.converter.UploadTemporarioOutputDataConverter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class UploadTemporarioUseCaseTest {

    @Test(expected = IllegalStateException.class)
    public void deveFalharSemCamposObrigatorios() {

        UploadTemporarioInputData inputData = new UploadTemporarioInputData();
        UploadTemporarioUseCaseImpl useCase = new
            UploadTemporarioUseCaseImpl(
            Mockito.mock(SistemaDeArquivosIntegration.class),
            Mockito.mock(UploadTemporarioOutputDataConverter.class));

        useCase.executar(inputData);
    }

    @Test
    public void deveRealizarUpload() {
        UploadTemporarioInputData inputData = UploadTemporarioInputData
            .builder()
            .nome("arquivo.pdf")
            .contentType("application/pdf")
            .content(new byte[]{})
            .build();

        SistemaDeArquivosIntegration sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);
        UploadTemporarioOutputDataConverter uploadTemporarioOutputDataConverter = Mockito.mock(UploadTemporarioOutputDataConverter.class);
        UploadTemporarioUseCaseImpl useCase = new
            UploadTemporarioUseCaseImpl(
            sistemaDeArquivosIntegration,
            uploadTemporarioOutputDataConverter);

        Mockito.when(sistemaDeArquivosIntegration.upload(any(Arquivo.class)))
            .thenReturn(Arquivo.builder().build());

        Mockito.when(uploadTemporarioOutputDataConverter.to(any(Arquivo.class)))
            .thenReturn(UploadTemporarioOutputData.builder().build());

        UploadTemporarioOutputData outputData = useCase.executar(inputData);

        Assert.assertNotNull(outputData);

        Mockito.verify(sistemaDeArquivosIntegration, Mockito.times(1)).upload(any(Arquivo.class));
        Mockito.verify(uploadTemporarioOutputDataConverter, Mockito.times(1)).to(any(Arquivo.class));
    }
}
