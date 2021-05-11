package br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.arquivo.download.converter.DownloadOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class DownloadUseCaseImpl implements DownloadUseCase {

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private DownloadOutputDataConverter outputDataConverter;

    @Override
    public DownloadOutputData executar(DownloadInputData inputData) {
        validarDadosEntrada(inputData);
        Arquivo arquivoComMetadata = buscarMetadata(inputData);
        Arquivo arquivoComContent = buscarArquivo(inputData);

        Arquivo arquivoCompleto = mescarArquivos(arquivoComMetadata, arquivoComContent);

        return outputDataConverter.to(arquivoCompleto);
    }

    private void validarDadosEntrada(DownloadInputData inputData) {
        Validator.of(inputData)
            .validate(DownloadInputData::getUri, Objects::nonNull, "A URI é nula")
            .get();
    }

    private Arquivo buscarMetadata(DownloadInputData inputData) {
        return sistemaDeArquivosIntegration.getMetadata(inputData.getUri());
    }

    private Arquivo buscarArquivo(DownloadInputData inputData) {
        return sistemaDeArquivosIntegration.download(inputData.getUri());
    }

    private Arquivo mescarArquivos(Arquivo arquivoComMetadata, Arquivo arquivoComContent) {
        return Arquivo
            .builder()
            .nome(arquivoComMetadata.getNome())
            .uri(arquivoComMetadata.getUri())
            .contentType(arquivoComContent.getContentType())
            .content(arquivoComContent.getContent())
            .build();
    }
}
