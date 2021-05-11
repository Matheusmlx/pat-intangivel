package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.converter.BuscarDocumentoMovimentacaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarDocumentoMovimentacaoUseCaseImpl implements BuscarDocumentoMovimentacaoUseCase {

    private DocumentoDataProvider documentoDataProvider;

    private BuscarDocumentoMovimentacaoOutputDataConverter buscarDocumentoMovimentacaoOutputDataConverter;

    @Override
    public BuscarDocumentoMovimentacaoOutputData executar(BuscarDocumentoMovimentacaoIntputData inputData) {
        validarDadosEntrada(inputData);

        List<Documento> documentosEncontrados = buscar(inputData);

        return buscarDocumentoMovimentacaoOutputDataConverter.to(documentosEncontrados);
    }

    private void validarDadosEntrada(BuscarDocumentoMovimentacaoIntputData entrada) {
        Validator.of(entrada)
            .validate(BuscarDocumentoMovimentacaoIntputData::getMovimentacaoId, Objects::nonNull, "O id da Movimentação é nulo")
            .get();
    }

    private List<Documento> buscar(BuscarDocumentoMovimentacaoIntputData intputData){
        return documentoDataProvider.buscarDocumentoPorMovimentacaoId(intputData.getMovimentacaoId());
    }
}
