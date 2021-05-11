package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.buscar.BuscarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarDocumentoOutputDataConverter {

    private static final BuscarDocumentoOutputDataConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new BuscarDocumentoOutputDataConverter.ItemOutputDataConverter();

    public BuscarDocumentoOutputData to(List<Documento> source) {
        List<BuscarDocumentoOutputData.Documento> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarDocumentoOutputData target = new BuscarDocumentoOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarDocumentoOutputData.Documento converterItem(Documento source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<Documento, BuscarDocumentoOutputData.Documento> {
        @Override
        public BuscarDocumentoOutputData.Documento to(Documento source) {
            BuscarDocumentoOutputData.Documento target = super.to(source);

            if (Objects.nonNull(source.getPatrimonio().getId())) {
                target.setIdPatrimonio(source.getPatrimonio().getId());
            }
            if (Objects.nonNull(source.getTipoDocumento().getId())) {
                target.setIdTipoDocumento(source.getTipoDocumento().getId());
            }
            if (Objects.nonNull(source.getData())) {
                target.setData(DateValidate.formatarData(source.getData()));
            }

            return target;

        }
    }

}
