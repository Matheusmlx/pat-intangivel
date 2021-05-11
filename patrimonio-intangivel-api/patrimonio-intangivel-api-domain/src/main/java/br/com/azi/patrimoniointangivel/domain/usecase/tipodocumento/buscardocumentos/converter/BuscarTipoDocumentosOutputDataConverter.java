package br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos.converter;

import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.domain.usecase.tipodocumento.buscardocumentos.BuscarTipoDocumentosOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;

public class BuscarTipoDocumentosOutputDataConverter {

    public BuscarTipoDocumentosOutputData to(List<TipoDocumento> from) {
        BuscarPatrimonioOutputDataItemConverter outputDataItemConverter = new BuscarPatrimonioOutputDataItemConverter();

        return BuscarTipoDocumentosOutputData
            .builder()
            .items(from
                .stream()
                .map(outputDataItemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    private static class BuscarPatrimonioOutputDataItemConverter extends GenericConverter<TipoDocumento, BuscarTipoDocumentosOutputData.TipoDocumento> {
        @Override
        public BuscarTipoDocumentosOutputData.TipoDocumento to(TipoDocumento source) {
            BuscarTipoDocumentosOutputData.TipoDocumento target = super.to(source);

            return target;
        }
    }
}
