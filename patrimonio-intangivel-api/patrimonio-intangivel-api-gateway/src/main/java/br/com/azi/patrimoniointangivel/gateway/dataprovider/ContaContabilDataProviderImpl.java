package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.ContaContabilConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ContaContabilEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.QContaContabilEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.QContaContabilProdutoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.ContaContabilRepository;
import br.com.azi.patrimoniointangivel.utils.AcetuacaoUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ContaContabilDataProviderImpl implements ContaContabilDataProvider {

    @Autowired
    private ContaContabilRepository repository;

    @Autowired
    private ContaContabilConverter converter;

    @Override
    public Optional<ContaContabil> buscarPorId(Long contaContabilId) {
        Optional<ContaContabilEntity> encontrada = repository.findById(contaContabilId);
        return encontrada.map(patrimonioEntity -> converter.to(patrimonioEntity));
    }

    @Override
    public Optional<ContaContabil> buscarPorCodigo(String codigo) {
        Optional<ContaContabilEntity> encontrada = repository.findByCodigo(codigo);
        return encontrada.map(contaContabilEntity -> converter.to(contaContabilEntity));
    }

    @Override
    public ListaPaginada<ContaContabil> buscarPorFiltro(ContaContabil.Filtro filtro) {
        QContaContabilEntity contaContabilQuery = QContaContabilEntity.contaContabilEntity;
        BooleanExpression expression = QContaContabilEntity.contaContabilEntity.id.isNotNull().and(contaContabilQuery.situacao.eq("ATIVO"));

        if (Objects.nonNull(filtro.getProdutoId())) {
            JPQLQuery subSelect = JPAExpressions.select(QContaContabilProdutoEntity.contaContabilProdutoEntity.contaContabilId)
                .from(QContaContabilProdutoEntity.contaContabilProdutoEntity)
                .where(QContaContabilProdutoEntity.contaContabilProdutoEntity.produtoId.eq(filtro.getProdutoId()));

            BooleanExpression conteudoExp = QContaContabilEntity.contaContabilEntity.id.in(subSelect);

            expression = expression.and(conteudoExp);
        }

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExp = contaContabilQuery.codigo.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(
                    contaContabilQuery.situacao
                        .trim()
                        .containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo())
                            .trim()))
                .or(
                    contaContabilQuery.codigo
                        .trim()
                        .contains(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                .or(
                    contaContabilQuery.descricao
                        .trim()
                        .containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()).trim())
                );

            expression = expression.and(conteudoExp);
        }

        Page<ContaContabilEntity> entidadesEncontradas = repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));

        List<ContaContabil> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<ContaContabil>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }
}
