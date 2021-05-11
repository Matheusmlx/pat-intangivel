package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.converter.EditarDocumentoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.exception.EditarDocumentoMovimentacaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.exception.NumeroUnicoException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarDocumentoMovimentacaoUseCaseImpl implements EditarDocumentoMovimentacaoUseCase {

    private DocumentoDataProvider documentoDataProvider;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private EditarDocumentoMovimentacaoOutputDataConverter outputDataConverter;

    @Override
    public EditarDocumentoMovimentacaoOutputData executar(EditarDocumentoMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);
        Documento documento = buscarDocumento(inputData);

        validarSeNumeroDocumentoUnico(inputData, documento);
        setarDados(documento, inputData);
        Documento documentoSalvo = atualizar(documento);
        promoverArquivoTemporario(documentoSalvo);

        return outputDataConverter.to(documentoSalvo);
    }

    private void validarDadosEntrada(EditarDocumentoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarDocumentoMovimentacaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarSeNumeroDocumentoUnico(EditarDocumentoMovimentacaoInputData inputData, Documento documento) {
        if (!documento.getId().equals(inputData.getId()) && documentoDataProvider.existeDocumentoMovimentacaoComNumero(inputData.getIdMovimentacao(), inputData.getNumero(), inputData.getIdTipoDocumento())) {
            throw new NumeroUnicoException();
        }

    }

    private Documento buscarDocumento(EditarDocumentoMovimentacaoInputData inputData) {
        return (documentoDataProvider.buscarPorId(inputData.getId()).orElseThrow(EditarDocumentoMovimentacaoException::new));
    }

    private void setarDados(Documento documento, EditarDocumentoMovimentacaoInputData inputData) {
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

        Optional.ofNullable(inputData.getIdMovimentacao()).ifPresent(idMovimentacao ->
            documento.setMovimentacao(Movimentacao.builder()
                .id(idMovimentacao)
                .build()));
    }

    private Documento atualizar(Documento documento) {
        return documentoDataProvider.atualizar(documento);
    }

    private void promoverArquivoTemporario(Documento documento) {
        sistemaDeArquivosIntegration.promote(Arquivo.builder()
            .uri(documento.getUriAnexo())
            .build());
    }
}
