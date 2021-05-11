package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.buscar.BuscarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarDocumentoMovimentacaoOutputDataConverter {

    private static final BuscarDocumentoMovimentacaoOutputDataConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new BuscarDocumentoMovimentacaoOutputDataConverter.ItemOutputDataConverter();

    public BuscarDocumentoMovimentacaoOutputData to(List<Documento> source) {
        List<BuscarDocumentoMovimentacaoOutputData.Documento> itens = source.stream()
            .map(this::converterItem)
            .collect(Collectors.toList());

        BuscarDocumentoMovimentacaoOutputData target = new BuscarDocumentoMovimentacaoOutputData();
        target.setItems(itens);

        return target;
    }

    private BuscarDocumentoMovimentacaoOutputData.Documento converterItem(Documento source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<Documento, BuscarDocumentoMovimentacaoOutputData.Documento> {
        @Override
        public BuscarDocumentoMovimentacaoOutputData.Documento to(Documento source) {
            BuscarDocumentoMovimentacaoOutputData.Documento target = super.to(source);

            if (Objects.nonNull(source.getPatrimonio().getId())) {
                target.setIdPatrimonio(source.getPatrimonio().getId());
            }
            if (Objects.nonNull(source.getTipoDocumento().getId())) {
                target.setIdTipoDocumento(source.getTipoDocumento().getId());
            }

            if(Objects.nonNull(source.getMovimentacao().getId())) {
                target.setIdMovimentacao(source.getMovimentacao().getId());
            }

            if (Objects.nonNull(source.getData())) {
                target.setData(DateValidate.formatarData(source.getData()));
            }

            return target;
        }
    }
}
