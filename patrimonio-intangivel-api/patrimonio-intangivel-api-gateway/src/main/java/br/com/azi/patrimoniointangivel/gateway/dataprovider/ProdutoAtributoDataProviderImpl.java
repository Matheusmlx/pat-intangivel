package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ProdutoAtributoDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ProdutoAtributoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.ProdutoAtributoReadOnlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProdutoAtributoDataProviderImpl implements ProdutoAtributoDataProvider {

    private static final long PRODUTO_ID = 401L;

    @Autowired
    private ProdutoAtributoReadOnlyRepository produtoAtributoReadOnlyRepository;

    @Override
    public String getValor(String atributo) {
        ProdutoAtributoEntity produtoAtributo = produtoAtributoReadOnlyRepository.findByProdutoIdAndAtributo(PRODUTO_ID, atributo);

        if (Objects.nonNull(produtoAtributo))
            return produtoAtributo.getValor();

        return null;
    }
}
