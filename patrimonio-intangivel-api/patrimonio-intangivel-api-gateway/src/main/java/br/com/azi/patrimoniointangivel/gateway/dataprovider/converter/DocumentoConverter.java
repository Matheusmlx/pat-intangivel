package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;


import br.com.azi.patrimoniointangivel.domain.entity.Documento;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.TipoDocumento;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.DocumentoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.MovimentacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.TipoDocumentoEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DocumentoConverter extends GenericConverter<DocumentoEntity, Documento> {

    @Override
    public DocumentoEntity from(Documento source) {
        DocumentoEntity documentoEntity = super.from(source);

        if (Objects.nonNull(source.getData())) {
            documentoEntity.setData(DateUtils.asDate(source.getData()));
        }

        if (Objects.nonNull(source.getPatrimonio().getId())) {
            PatrimonioEntity patrimonioEntity = new PatrimonioEntity();
            patrimonioEntity.setId(source.getPatrimonio().getId());
            documentoEntity.setPatrimonio(patrimonioEntity);
        }

        if (Objects.nonNull(source.getMovimentacao())) {
            MovimentacaoEntity movimentacaoEntity = new MovimentacaoEntity();
            movimentacaoEntity.setId(source.getMovimentacao().getId());
            documentoEntity.setMovimentacao(movimentacaoEntity);
        }

        if (Objects.nonNull(source.getTipoDocumento().getId())) {
            TipoDocumentoEntity tipoDocumentoEntity = new TipoDocumentoEntity();
            tipoDocumentoEntity.setId(source.getTipoDocumento().getId());
            documentoEntity.setTipoDocumento(tipoDocumentoEntity);
        }

        return documentoEntity;
    }

    @Override
    public Documento to(DocumentoEntity documentoEntity) {
        Documento documento = super.to(documentoEntity);

        if (Objects.nonNull(documentoEntity.getData())) {
            documento.setData(DateUtils.asLocalDateTime(documentoEntity.getData()));
        }

        if (Objects.nonNull(documentoEntity.getMovimentacao())) {
            documento.setMovimentacao(movimentacaoJpaEntitytoPatrimonioBusinessEntity(documentoEntity.getMovimentacao()));
        }

        documento.setPatrimonio(patrimonioJpaEntitytoPatrimonioBusinessEntity(documentoEntity.getPatrimonio()));
        documento.setTipoDocumento(tipoDocumentoJpaEntitytoTipoDocumentoBusinessEntity(documentoEntity.getTipoDocumento()));
        return documento;
    }

    private Patrimonio patrimonioJpaEntitytoPatrimonioBusinessEntity(PatrimonioEntity jpa) {
        if (Objects.nonNull(jpa)) {
            return Patrimonio.builder()
                .id(jpa.getId())
                .build();
        }
        return null;
    }

    private Movimentacao movimentacaoJpaEntitytoPatrimonioBusinessEntity(MovimentacaoEntity jpa) {
        if (Objects.nonNull(jpa)) {
            return Movimentacao.builder()
                .id(jpa.getId())
                .build();
        }
        return null;
    }

    private TipoDocumento tipoDocumentoJpaEntitytoTipoDocumentoBusinessEntity(TipoDocumentoEntity jpa) {
        if (Objects.nonNull(jpa)) {
            return TipoDocumento.builder()
                .id(jpa.getId())
                .build();
        }
        return null;
    }

}
