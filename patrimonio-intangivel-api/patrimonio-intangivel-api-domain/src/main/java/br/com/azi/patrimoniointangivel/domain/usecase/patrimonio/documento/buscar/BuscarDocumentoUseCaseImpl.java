package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.converter.BuscarDocumentoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarDocumentoUseCaseImpl implements BuscarDocumentoUseCase {

    private DocumentoDataProvider documentoDataProvider;

    private BuscarDocumentoOutputDataConverter outputDataConverter;

    @Override
    public BuscarDocumentoOutputData executar(BuscarDocumentoInputData inputData) {
        validarDadosEntrada(inputData);

        List<Documento> documentosEncontrados = buscar(inputData);

        return outputDataConverter.to(documentosEncontrados);
    }

    private void validarDadosEntrada(BuscarDocumentoInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarDocumentoInputData::getPatrimonioId, Objects::nonNull, "O id do patrimonio é nulo")
            .get();
    }

    private List<Documento> buscar(BuscarDocumentoInputData inputData) {
        return documentoDataProvider.buscarDocumentoPorPatrimonioId(inputData.getPatrimonioId());
    }
}
