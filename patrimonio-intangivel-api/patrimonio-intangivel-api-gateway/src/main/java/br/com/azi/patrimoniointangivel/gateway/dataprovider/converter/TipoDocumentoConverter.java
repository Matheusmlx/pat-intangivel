package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.TipoDocumentoEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class TipoDocumentoConverter extends GenericConverter<TipoDocumentoEntity, TipoDocumento> {

    @Override
    public TipoDocumento to(TipoDocumentoEntity tipoDocumentoEntity) {
        TipoDocumento tipoDocumento = super.to(tipoDocumentoEntity);
        tipoDocumento.setId(tipoDocumentoEntity.getId());

        tipoDocumento.setDescricao(tipoDocumentoEntity.getDescricao());
        tipoDocumento.setIdentificacaoDocumento(tipoDocumentoEntity.getIdentificacaoDocumento());
        tipoDocumento.setPermiteAnexo(tipoDocumentoEntity.getPermiteAnexo());
        return tipoDocumento;
    }

}
