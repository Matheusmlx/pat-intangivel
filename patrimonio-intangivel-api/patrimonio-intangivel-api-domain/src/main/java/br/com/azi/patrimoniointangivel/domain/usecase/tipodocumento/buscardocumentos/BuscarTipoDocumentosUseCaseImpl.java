package br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos;

import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos.converter.BuscarTipoDocumentosOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BuscarTipoDocumentosUseCaseImpl implements BuscarTipoDocumentosUseCase {

    private TipoDocumentoDataprovider tipoDocumentoDataProvider;

    private BuscarTipoDocumentosOutputDataConverter outputDataConverter;

    @Override
    public BuscarTipoDocumentosOutputData executar() {
        List<TipoDocumento> entidadesEncontradas = buscar();

        return outputDataConverter.to(entidadesEncontradas);
    }

    private List<TipoDocumento> buscar() {
        return tipoDocumentoDataProvider.buscar();
    }
}
