package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.FiltroBase;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.MovimentacaoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.MovimentacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.QMovimentacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.MovimentacaoRepository;
import br.com.azi.patrimoniointangivel.utils.AcetuacaoUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MovimentacaoDataProviderImpl implements MovimentacaoDataProvider {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private MovimentacaoConverter converter;

    @Autowired
    private EntityManager entityManager;

    private static Page<MovimentacaoEntity> construirPaginacao(List<MovimentacaoEntity> movimentacoes, Pageable pageable, Long totalRegistros){
        return new PageImpl<>(movimentacoes, pageable, totalRegistros);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public Movimentacao salvar(Movimentacao movimentacao) {
        MovimentacaoEntity salva = repository.save(converter.from(movimentacao));
        return converter.to(salva);
    }

    @Override
    public boolean existe(Long id){
        return repository.existsById(id);
    }

    @Override
    public boolean existePorIdPatrimonio(Long idPatrimonio){
        return repository.existsByPatrimonioId(idPatrimonio);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public Optional<Movimentacao> buscarPorId(Long id){
        Optional<MovimentacaoEntity> encontrada = repository.findById(id);
        return encontrada.map(movimentacaoEntity -> converter.to(movimentacaoEntity));
    }
    @Override
    public Optional<Movimentacao> buscarUltimoCriado() {
        Optional<MovimentacaoEntity> encontrada = repository.findFirstByCodigoNotNullOrderByCodigoDesc();
        return encontrada.map(movimentacaoEntity -> converter.to(movimentacaoEntity));
    }
    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public ListaPaginada<Movimentacao> buscarMovimentacoesPorPatrimonio(Long idPatrimonio, Long size) {

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<MovimentacaoEntity> query = jpaQueryFactory.selectFrom(QMovimentacaoEntity.movimentacaoEntity).where(QMovimentacaoEntity.movimentacaoEntity.patrimonio.id.eq(idPatrimonio)).orderBy(new OrderSpecifier(Order.DESC,QMovimentacaoEntity.movimentacaoEntity.dataCadastro));

        query.limit(size);

        QueryResults<MovimentacaoEntity> results = query.fetchResults();

        List<Movimentacao> entidades = results
            .getResults()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Movimentacao>builder()
            .items(entidades)
            .build();
    }

    @Override
    public ListaPaginada<Movimentacao> buscarPorFiltro(Movimentacao.Filtro filtro) {
        QMovimentacaoEntity movimentacaoEntity = QMovimentacaoEntity.movimentacaoEntity;
        BooleanExpression expression = movimentacaoEntity.id.isNotNull();

        if(!StringUtils.isEmpty(filtro.getConteudo())){
            BooleanExpression conteudoExp =
                movimentacaoEntity.situacao.trim().containsIgnoreCase(filtro.getConteudo().trim())
                    .or(movimentacaoEntity.codigo.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                    .or(movimentacaoEntity.tipo.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                    .or(movimentacaoEntity.orgaoOrigem.sigla.trim().containsIgnoreCase(filtro.getConteudo().trim()))
                    .or(movimentacaoEntity.orgaoDestino.sigla.trim().containsIgnoreCase(filtro.getConteudo().trim()))
                    .or(movimentacaoEntity.situacao.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()).trim()));

            expression = expression.and(conteudoExp);
        }

       if(!StringUtils.isEmpty(filtro.getConteudoExtra())){
           BooleanExpression conteudoExp = movimentacaoEntity.situacao.trim().containsIgnoreCase(filtro.getConteudoExtra());
           expression = expression.and(conteudoExp);
       }


        if (Objects.nonNull(filtro.getUnidadeOrganizacionalIds())) {
            if (!StringUtils.isEmpty(filtro.getConteudo()) && filtro.getConteudo().equals("AGUARDANDO_RECEBIMENTO")) {
                BooleanExpression conteudoExp = movimentacaoEntity.orgaoDestino.id.in(filtro.getUnidadeOrganizacionalIds());

                expression = expression.and(conteudoExp);
            } else {
                BooleanExpression conteudoExp = movimentacaoEntity.orgaoDestino.id.in(filtro.getUnidadeOrganizacionalIds())
                    .or(movimentacaoEntity.orgaoOrigem.id.in(filtro.getUnidadeOrganizacionalIds()));

                expression = expression.and(conteudoExp);
            }
        }


        Page<MovimentacaoEntity> entidadesEncontradas = buscarComOrdenacao(expression,filtro);

        List<Movimentacao> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Movimentacao>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    private Page<MovimentacaoEntity> buscarComOrdenacao(BooleanExpression expression, FiltroBase filtro){
        return buscarComOrdenacaoPorSituacao(expression,filtro);
    }

    private Page<MovimentacaoEntity> buscarComOrdenacaoPorSituacao(BooleanExpression expression, FiltroBase filtro) {
        Expression<Integer> cases = new CaseBuilder()
            .when(QMovimentacaoEntity.movimentacaoEntity.situacao.eq(Movimentacao.Situacao.EM_ELABORACAO.name())).then(1)
            .when(QMovimentacaoEntity.movimentacaoEntity.situacao.eq(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO.name())).then(2)
            .when(QMovimentacaoEntity.movimentacaoEntity.situacao.eq(Movimentacao.Situacao.FINALIZADO.name())).then(3)
            .when(QMovimentacaoEntity.movimentacaoEntity.situacao.eq(Movimentacao.Situacao.REJEITADO.name())).then(4)
            .otherwise(5);

        OrderSpecifier[] ordemPriorizacao;

        if (filtro.getSort().equalsIgnoreCase("situacao")) {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier(Order.valueOf(filtro.getDirection()), cases),
                new OrderSpecifier(Order.DESC, QMovimentacaoEntity.movimentacaoEntity.dataAlteracao)
            };
        } else {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier(Order.valueOf(filtro.getDirection()), (Expression) retornaOrdenacao(filtro.getSort())),
                new OrderSpecifier(Order.DESC, QMovimentacaoEntity.movimentacaoEntity.dataAlteracao)
            };
        }

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<MovimentacaoEntity> query = jpaQueryFactory.selectFrom(QMovimentacaoEntity.movimentacaoEntity).where(expression).orderBy(ordemPriorizacao);
        query.offset(filtro.getPage() * filtro.getSize());
        query.limit(filtro.getSize());

        QueryResults<MovimentacaoEntity> results = query.fetchResults();
        return construirPaginacao(results.getResults(), FiltroConverter.extrairPaginacao(filtro), results.getTotal());
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    private Object retornaOrdenacao(String sort) {
        switch (sort) {
            case "codigo":
                return QMovimentacaoEntity.movimentacaoEntity.codigo;
            case "dataCadastro":
                return QMovimentacaoEntity.movimentacaoEntity.dataCadastro;
            case "tipo":
                return QMovimentacaoEntity.movimentacaoEntity.tipo;
            case "orgaoOrigem":
                return QMovimentacaoEntity.movimentacaoEntity.orgaoOrigem.sigla;
            case "orgaoDestino":
                return QMovimentacaoEntity.movimentacaoEntity.orgaoDestino.sigla;
            default:
                return QMovimentacaoEntity.movimentacaoEntity.dataAlteracao;
        }
    }
}
