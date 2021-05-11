package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.AmortizacaoConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.AmortizacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.QAmortizacaoEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.AmortizacaoRepository;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class AmortizacaoDataProviderImpl implements AmortizacaoDataProvider {

    @Autowired
    AmortizacaoRepository repository;

    @Autowired
    AmortizacaoConverter converter;

    @Autowired
    private EntityManager entityManager;

    @Override
    public Boolean existePorPatrimonio(Long patrimonioId) {
        return repository.existsByPatrimonio_Id(patrimonioId);
    }

    @Override
    public Amortizacao salvar(Amortizacao business) {
        AmortizacaoEntity salva = repository.save(converter.from(business));
        return converter.to(salva);
    }

    @Override
    @Transactional
    public Optional<Amortizacao> buscarUltimaPorPatrimonio(Long patrimonioId) {
        Optional<AmortizacaoEntity> encontrada = repository.findFirstByPatrimonio_IdOrderByDataFinalDesc(patrimonioId);
        return encontrada.map(converter::to);
    }

    @Override
    @Transactional
    public Optional<Amortizacao> buscarPrimeiraPorOrgaoEPatrimonio(Long orgaoId, Long patrimonioId) {
        Optional<AmortizacaoEntity> encontrada = repository.findFirstByOrgaoIdAndPatrimonioIdOrderByDataFinalAsc(orgaoId, patrimonioId);
        return encontrada.map(converter::to);
    }

    @Override
    @Transactional
    public Optional<Amortizacao> buscarPorPatrimonioEDataLimite(Long patrimonioId, LocalDateTime dataReferencia) {
        Optional<AmortizacaoEntity> encontrada = repository.findFirstByPatrimonio_IdAndDataFinalIsLessThanEqualOrderByDataFinalDesc(patrimonioId, DateUtils.asDate(dataReferencia));
        return encontrada.map(converter::to);
    }


    @Override
    public List<Amortizacao> buscar(Long patrimonioId) {
        List<AmortizacaoEntity> results = repository.findByPatrimonio_IdOrderByDataFinal(patrimonioId);
        return results
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());
    }

    public BigDecimal buscarValorSubtraidoPorPatrimonioId(Long patrimonioId) {
        List<Amortizacao> amortizacoes = buscar(patrimonioId);
        if (!amortizacoes.isEmpty()) {
            Amortizacao amortizacao = amortizacoes.get(amortizacoes.size() - 1);
            return amortizacao.getValorSubtraido();
        }
        return BigDecimal.valueOf(0);
    }

    @Override
    public Boolean existePorPatrimonioNoPeriodo(Long patrimonioId, LocalDateTime mesReferencia) {
        QAmortizacaoEntity amortizacaoQuery = QAmortizacaoEntity.amortizacaoEntity;
        BooleanExpression expression = amortizacaoQuery.patrimonio.id.isNotNull();
        BooleanExpression conteudoExp = amortizacaoQuery.patrimonio.id.eq(patrimonioId)
            .and(amortizacaoQuery.dataInicial.month().eq(mesReferencia.getMonthValue()))
            .and( amortizacaoQuery.dataInicial.year().eq(mesReferencia.getYear()));

        expression = expression.and(conteudoExp);

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<AmortizacaoEntity> query = jpaQueryFactory.selectFrom(QAmortizacaoEntity.amortizacaoEntity).where(expression);
        QueryResults<AmortizacaoEntity> results = query.fetchResults();

        return results.getTotal() > 0;
    }


    @Override
    public Boolean existePorPatrimonioEOrgaoNoPeriodo(Long patrimonioId, Long orgaoId, LocalDateTime mesReferencia) {
        QAmortizacaoEntity amortizacaoQuery = QAmortizacaoEntity.amortizacaoEntity;
        BooleanExpression expression = amortizacaoQuery.patrimonio.id.isNotNull();
        BooleanExpression conteudoExp = amortizacaoQuery.patrimonio.id.eq(patrimonioId)
            .and(amortizacaoQuery.orgao.id.eq(orgaoId))
            .and(amortizacaoQuery.dataInicial.month().eq(mesReferencia.getMonthValue()))
            .and( amortizacaoQuery.dataInicial.year().eq(mesReferencia.getYear()));

        expression = expression.and(conteudoExp);

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<AmortizacaoEntity> query = jpaQueryFactory.selectFrom(QAmortizacaoEntity.amortizacaoEntity).where(expression);
        QueryResults<AmortizacaoEntity> results = query.fetchResults();

        return results.getTotal() > 0;
    }

    @Override
    public Boolean existePorPatrimonioAteDataLimite(Long patrimonioId, LocalDateTime mesReferencia) {
        return repository.existsByPatrimonio_IdAndDataFinalLessThanEqual(patrimonioId, DateUtils.asDate(mesReferencia));
    }
}
