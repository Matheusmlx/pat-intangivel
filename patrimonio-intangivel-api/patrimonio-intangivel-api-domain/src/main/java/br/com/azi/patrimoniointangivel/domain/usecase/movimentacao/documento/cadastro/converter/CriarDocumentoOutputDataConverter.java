package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.documento.cadastro.CriarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;

import java.util.Objects;

public class CriarDocumentoOutputDataConverter extends GenericConverter<Documento, CriarDocumentoMovimentacaoOutputData> {
    @Override
    public CriarDocumentoMovimentacaoOutputData to(Documento source) {
        CriarDocumentoMovimentacaoOutputData target = super.to(source);
        if (Objects.nonNull(source.getMovimentacao().getId())) {
            target.setIdMovimentacao(source.getMovimentacao().getId());
        }
        if (Objects.nonNull(source.getTipoDocumento())) {
            target.setIdTipoDocumento(source.getTipoDocumento().getId());
        }
        if(Objects.nonNull(source.getData())){
            target.setData(DateValidate.formatarData(source.getData()));
        }

        return target;
    }
}
