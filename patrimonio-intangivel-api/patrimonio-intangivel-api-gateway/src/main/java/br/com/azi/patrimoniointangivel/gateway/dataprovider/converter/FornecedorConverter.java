package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.FornecedorEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class FornecedorConverter extends GenericConverter<FornecedorEntity, Fornecedor> {

    @Override
    public Fornecedor to(FornecedorEntity source) {
        Fornecedor target = super.to(source);

        target.setNome(source.getRazaoSocial());

        return target;
    }
}
