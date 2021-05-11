package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.expection.NumeroUnicoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.converter.EditarDocumentoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.exception.EditarDocumentoException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarDocumentoUseCaseImpl implements EditarDocumentoUseCase {

    private DocumentoDataProvider documentoDataProvider;

    private EditarDocumentoOutputDataConverter outputDataConverter;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public EditarDocumentoOutputData executar(EditarDocumentoInputData inputData) {
        validarDadosEntrada(inputData);
        validarSeNumeroDocumentoUnico(inputData);

        Documento documento = buscar(inputData);
        setarDados(documento, inputData);
        Documento documentoSalvo = atualizar(documento);
        promoverArquivoTemporario(documentoSalvo);

        return outputDataConverter.to(documentoSalvo);
    }

    private void validarDadosEntrada(EditarDocumentoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarDocumentoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarSeNumeroDocumentoUnico(EditarDocumentoInputData inputData) {
        if (documentoDataProvider.existeDocumentoComNumero(inputData.getIdPatrimonio(), inputData.getNumero(), inputData.getIdTipoDocumento())) {
            throw new NumeroUnicoException();
        }
    }

    private Documento buscar(EditarDocumentoInputData inputData) {
        return (documentoDataProvider.buscarPorId(inputData.getId()).orElseThrow(EditarDocumentoException::new));
    }

    private void setarDados(Documento documento, EditarDocumentoInputData inputData) {
        documento.setUriAnexo(inputData.getUriAnexo());
        documento.setValor(inputData.getValor());
        if (Objects.nonNull(inputData.getData())) {
            documento.setData(LocalDateTime.ofInstant(inputData.getData().toInstant(), ZoneOffset.UTC));
        }

        Optional.ofNullable(inputData.getNumero()).ifPresent(documento::setNumero);

        Optional.ofNullable(inputData.getIdTipoDocumento()).ifPresent(tipoDocumento ->
            documento.setTipoDocumento(TipoDocumento.builder()
            .id(tipoDocumento)
            .build()));

        Optional.ofNullable(inputData.getIdPatrimonio()).ifPresent(idPatrimonio ->
            documento.setPatrimonio(Patrimonio.builder()
            .id(idPatrimonio)
            .build()));
    }

    private void promoverArquivoTemporario(Documento documento) {
        sistemaDeArquivosIntegration.promote(Arquivo.builder()
            .uri(documento.getUriAnexo())
            .build());
    }

    private Documento atualizar(Documento documento) {
        return documentoDataProvider.atualizar(documento);
    }
}
