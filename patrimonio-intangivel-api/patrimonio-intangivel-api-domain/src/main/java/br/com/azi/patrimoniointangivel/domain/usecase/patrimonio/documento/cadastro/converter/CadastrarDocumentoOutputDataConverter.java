package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.documento.cadastro.CadastrarDocumentoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;

public class CadastrarDocumentoOutputDataConverter extends GenericConverter<Documento, CadastrarDocumentoOutputData> {
    @Override
    public CadastrarDocumentoOutputData to(Documento source) {
        CadastrarDocumentoOutputData target = super.to(source);
        if (Objects.nonNull(source.getPatrimonio().getId())) {
            target.setIdPatrimonio(source.getPatrimonio().getId());
        }
        if (Objects.nonNull(source.getTipoDocumento())) {
            target.setIdTipoDocumento(source.getTipoDocumento().getId());
        }
        if(source.getData()!=null){
            target.setData(DateValidate.formatarData(source.getData()));
        }

        return target;
    }
}
