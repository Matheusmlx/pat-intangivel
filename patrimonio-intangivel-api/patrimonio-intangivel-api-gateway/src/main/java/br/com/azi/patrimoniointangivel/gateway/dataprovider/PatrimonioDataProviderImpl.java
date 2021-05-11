package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.FiltroBase;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.PatrimoniosAgrupados;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.PatrimonioConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.PatrimoniosAgrupadosConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.PatrimoniosAgrupadosEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.QPatrimonioEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.QUnidadeOrganizacionalEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.PatrimonioRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PatrimonioDataProviderImpl implements PatrimonioDataProvider {

    @Autowired
    private PatrimonioRepository repository;

    @Autowired
    private PatrimonioConverter converter;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PatrimoniosAgrupadosConverter patrimonioAgrupadoConverter;

    private static Page<PatrimonioEntity> construirPaginacao(List<PatrimonioEntity> patrimonios, Pageable pageable, Long totalRegistros) {
        return new PageImpl<>(patrimonios, pageable, totalRegistros);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public Patrimonio salvar(Patrimonio business) {
        PatrimonioEntity salva = repository.save(converter.from(business));
        return converter.to(salva);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public Optional<Patrimonio> buscarPorId(Long id) {
        Optional<PatrimonioEntity> encontrada = repository.findById(id);
        return encontrada.map(patrimonioEntity -> converter.to(patrimonioEntity));
    }

    @Override
    public ListaPaginada<Patrimonio> buscarPorFiltro(Patrimonio.Filtro filtro) {
        QPatrimonioEntity patrimonioQuery = QPatrimonioEntity.patrimonioEntity;
        BooleanExpression expression = patrimonioQuery.id.isNotNull();

        if (!StringUtils.isEmpty(filtro.getConteudo())) {
            BooleanExpression conteudoExp = patrimonioQuery.nome.trim().containsIgnoreCase(filtro.getConteudo().trim())
                .or(patrimonioQuery.situacao.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()).trim()))
                .or(patrimonioQuery.numero.trim().contains(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo().trim())))
                .or(patrimonioQuery.id.like(filtro.getConteudo().trim()).and(patrimonioQuery.numero.isNull())
                )
                .or(patrimonioQuery.tipo.trim().containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(filtro.getConteudo()).trim()))
                .or(patrimonioQuery.orgao.sigla.trim().containsIgnoreCase(filtro.getConteudo().trim()));

            expression = expression.and(conteudoExp);
        }

        if (Objects.nonNull(filtro.getUnidadeOrganizacionalIds())) {
            BooleanExpression conteudoExp = patrimonioQuery.orgao.id.in(filtro.getUnidadeOrganizacionalIds())
                .or(patrimonioQuery.orgao.isNull());

            expression = expression.and(conteudoExp);
        }

        Page<PatrimonioEntity> entidadesEncontradas = buscarComOrdenacao(expression, filtro);

        List<Patrimonio> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Patrimonio>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();
    }

    private Page<PatrimonioEntity> buscarComOrdenacao(BooleanExpression expression, FiltroBase filtro) {
        OrderSpecifier[] ordemPriorizacao;
        Expression<Integer> cases = new CaseBuilder()
            .when(QPatrimonioEntity.patrimonioEntity.situacao.eq(Patrimonio.Situacao.BAIXADO.name())).then(1)
            .when(QPatrimonioEntity.patrimonioEntity.situacao.eq(Patrimonio.Situacao.ATIVO.name())).then(2)
            .when(QPatrimonioEntity.patrimonioEntity.situacao.eq(Patrimonio.Situacao.EM_ELABORACAO.name())).then(3)
            .otherwise(4);

        if (filtro.getSort().equalsIgnoreCase("situacao")) {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier(Order.valueOf(filtro.getDirection()), cases),
                new OrderSpecifier(Order.DESC, QPatrimonioEntity.patrimonioEntity.dataAlteracao)
            };
        } else if (filtro.getSort().equalsIgnoreCase("codigo")) {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier(Order.valueOf("ASC"), cases),
                new OrderSpecifier(Order.valueOf(filtro.getDirection()), (Expression) retornaOrdenacao(filtro.getSort())),
                new OrderSpecifier(Order.DESC, QPatrimonioEntity.patrimonioEntity.dataAlteracao)
            };
        } else {
            ordemPriorizacao = new OrderSpecifier[]{
                new OrderSpecifier(Order.valueOf(filtro.getDirection()), (Expression) retornaOrdenacao(filtro.getSort())),
                new OrderSpecifier(Order.DESC, QPatrimonioEntity.patrimonioEntity.dataAlteracao)
            };
        }

        return buscarPatrimonios(expression, ordemPriorizacao, filtro);
    }

    private Page<PatrimonioEntity> buscarPatrimonios(BooleanExpression expression, OrderSpecifier[] ordemPriorizacao, FiltroBase filtro) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        QPatrimonioEntity patrimonioEntity = QPatrimonioEntity.patrimonioEntity;
        QUnidadeOrganizacionalEntity unidadeOrganizacionalEntity = QUnidadeOrganizacionalEntity.unidadeOrganizacionalEntity;

        JPAQuery<PatrimonioEntity> query = jpaQueryFactory.selectFrom(patrimonioEntity)
            .leftJoin(patrimonioEntity.orgao, unidadeOrganizacionalEntity)
            .where(expression).orderBy(ordemPriorizacao).fetchJoin();
        query.offset(filtro.getPage() * filtro.getSize());
        query.limit(filtro.getSize());

        QueryResults<PatrimonioEntity> results = query.fetchResults();

        return construirPaginacao(results.getResults(), FiltroConverter.extrairPaginacao(filtro), results.getTotal());
    }

    private Object retornaOrdenacao(String sort) {
        switch (sort) {
            case "id":
                return QPatrimonioEntity.patrimonioEntity.id;
            case "orgao":
                return QPatrimonioEntity.patrimonioEntity.orgao.sigla;
            case "codigo":
                return QPatrimonioEntity.patrimonioEntity.numero;
            case "nome":
                return QPatrimonioEntity.patrimonioEntity.nome.lower();
            case "tipo":
                return QPatrimonioEntity.patrimonioEntity.tipo;
            default:
                return QPatrimonioEntity.patrimonioEntity.dataAlteracao;
        }
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public Patrimonio atualizar(Patrimonio business) {
        PatrimonioEntity salvo = repository.save(converter.from(business));
        return converter.to(salvo);
    }

    @Override
    @Transactional
    public boolean existe(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Modifying(flushAutomatically = true)
    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    @Modifying(flushAutomatically = true)
    @Override
    @Transactional
    public Optional<Patrimonio> buscarUltimoAtivado() {
        Optional<PatrimonioEntity> encontrada = repository.findFirstByNumeroNotNullOrderByNumeroDesc();
        return encontrada.map(patrimonioEntity -> converter.to(patrimonioEntity));
    }

    @Override
    @Transactional
    @Modifying(flushAutomatically = true)
    public List<Patrimonio> buscarPatrimoniosAmortizaveis() {

        List<PatrimonioEntity> results = repository.findBySituacaoEqualsAndValorLiquidoIsAfterAndAmortizavelIsTrue(
            Patrimonio.Situacao.ATIVO.name(), BigDecimal.ZERO);

        return results
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Modifying(flushAutomatically = true)
    public List<Patrimonio> buscarPatrimoniosAmortizaveisPorOrgao(Long orgaoId) {
        List<PatrimonioEntity> results = repository.findBySituacaoEqualsAndValorLiquidoIsAfterAndOrgao_IdEqualsAndAmortizavelIsTrue(
            Patrimonio.Situacao.ATIVO.name(), BigDecimal.ZERO, orgaoId);

        return results
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Modifying(flushAutomatically = true)
    @Override
    @Transactional
    public Optional<Patrimonio> buscarUltimoPorMemorando() {
        Optional<PatrimonioEntity> encontrada = repository.findFirstByNumeroMemorandoNotNullOrderByNumeroMemorandoDesc();
        return encontrada.map(patrimonioEntity -> converter.to(patrimonioEntity));
    }

    @Override
    public Long contarTotalDeRegistrosPorOrgaos(List<Long> idsOrgao) {
        return repository.countAllByOrgao_IdInOrOrgaoIsNull(idsOrgao);
    }

    @Override
    public Long contarEmElaboracaoPorOrgaos(List<Long> idsOrgao) {
        return repository.countAllBySituacaoEqualsAndOrgao_IdInOrOrgaoIsNull(Patrimonio.Situacao.EM_ELABORACAO.name(), idsOrgao);
    }

    @Override
    public Long contarAtivosPorOrgaos(List<Long> idsOrgao) {
        return repository.countAllBySituacaoEqualsAndOrgao_IdIn(Patrimonio.Situacao.ATIVO.name(), idsOrgao);
    }

    @Override
    public Long contarPorTipoEOrgaos(String tipo, List<Long> idsOrgao) {
        return repository.countAllByTipoEqualsAndOrgao_IdInOrOrgaoIsNull(tipo, idsOrgao);
    }

    @Override
    public List<Patrimonio> buscarProximosPatrimoniosAVencerNosOrgaos(List<Long> idsOrgao, Long numeroDeRegistrosRetornados) {
        Page<PatrimonioEntity> results = repository.findAllBySituacaoEqualsAndOrgao_IdInAndFimVidaUtilIsGreaterThanEqualOrderByFimVidaUtilAsc(Patrimonio.Situacao.ATIVO.name(),
            idsOrgao, new Date(), PageRequest.of(0, numeroDeRegistrosRetornados.intValue()));

        return results
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    @Override
    public List<PatrimoniosAgrupados> buscarPatrimoniosAgrupadosPorOrgaoETipo(List<Long> idsOrgao) {
        List<PatrimoniosAgrupadosEntity> results = repository.buscarPatrimoniosAgrupadosPorOrgaoETipo(idsOrgao);

        return results
            .stream()
            .map(patrimonioAgrupadoConverter::to)
            .collect(Collectors.toList());
    }
}
