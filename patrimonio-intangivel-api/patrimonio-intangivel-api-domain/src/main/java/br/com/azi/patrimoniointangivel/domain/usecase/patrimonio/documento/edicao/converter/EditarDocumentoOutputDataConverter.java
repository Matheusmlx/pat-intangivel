package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.edicao.EditarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;

import java.util.Objects;

public class EditarDocumentoOutputDataConverter extends GenericConverter<Documento, EditarDocumentoOutputData> {
    @Override
    public EditarDocumentoOutputData to(Documento source) {
        EditarDocumentoOutputData target = super.to(source);
        if (Objects.nonNull(source.getPatrimonio().getId())) {
            target.setIdPatrimonio(source.getPatrimonio().getId());
        }
        if (Objects.nonNull(source.getTipoDocumento().getId())) {
            target.setIdTipoDocumento(source.getTipoDocumento().getId());
        }
        return target;
    }
}
