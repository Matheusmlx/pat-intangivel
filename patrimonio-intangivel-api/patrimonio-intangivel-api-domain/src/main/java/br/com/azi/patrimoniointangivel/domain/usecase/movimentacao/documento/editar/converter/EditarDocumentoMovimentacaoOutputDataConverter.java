package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.editar.EditarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.Objects;

public class EditarDocumentoMovimentacaoOutputDataConverter extends GenericConverter<Documento, EditarDocumentoMovimentacaoOutputData> {

    @Override
    public EditarDocumentoMovimentacaoOutputData to(Documento source) {
        EditarDocumentoMovimentacaoOutputData target = super.to(source);
        if (Objects.nonNull(source.getMovimentacao().getId())) {
            target.setIdMovimentacao(source.getMovimentacao().getId());
        }
        if (Objects.nonNull(source.getTipoDocumento())) {
            target.setIdTipoDocumento(source.getTipoDocumento().getId());
        }

        return target;
    }
}
