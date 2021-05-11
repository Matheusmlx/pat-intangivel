package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.ContaContabilEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.NotaLancamentoContabilEntity;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PatrimonioConverter extends GenericConverter<PatrimonioEntity, Patrimonio> {

    @Autowired
    ConfigAmortizacaoConverter configAmortizacaoConverter;

    @Autowired
    ContaContabilConverter contaContabilConverter;

    @Autowired
    DadosAmortizacaoConverter dadosAmortizacaoConverter;

    @Autowired
    FornecedorConverter fornecedorConverter;

    @Autowired
    UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Autowired
    private NotaLancamentoContabilConverter notaLancamentoContabilConverter;

    @Override
    public PatrimonioEntity from(Patrimonio source) {
        PatrimonioEntity target = super.from(source);

        if (Objects.nonNull(source.getDataAtivacao())) {
            target.setDataAtivacao(DateUtils.asDate(source.getDataAtivacao()));
        }

        if (Objects.nonNull(source.getDataAquisicao())) {
            target.setDataAquisicao(DateUtils.asDate(source.getDataAquisicao()));
        }

        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNotaLancamentoContabil(
                NotaLancamentoContabilEntity
                    .builder()
                    .numero(source.getNotaLancamentoContabil().getNumero())
                    .dataLancamento(DateUtils.asDate(source.getNotaLancamentoContabil().getDataLancamento()))
                    .build());
        }

        if (Objects.nonNull(source.getInicioVidaUtil())) {
            target.setInicioVidaUtil(DateUtils.asDate(source.getInicioVidaUtil()));
        }

        if (Objects.nonNull(source.getDataVencimento())) {
            target.setDataVencimento(DateUtils.asDate(source.getDataVencimento()));
        }

        if (Objects.nonNull(source.getFimVidaUtil())) {
            target.setFimVidaUtil(DateUtils.asDate(source.getFimVidaUtil()));
        }

        if (Objects.nonNull(source.getDataFinalAtivacao())) {
            target.setDataFinalAtivacao(DateUtils.asDate(source.getDataFinalAtivacao()));
        }

        if (Objects.nonNull(source.getDataCadastro())) {
            target.setDataCadastro(DateUtils.asDate(source.getDataCadastro()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.from(source.getOrgao()));
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(unidadeOrganizacionalConverter.from(source.getSetor()));
        }

        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNotaLancamentoContabil(notaLancamentoContabilConverter.from(source.getNotaLancamentoContabil()));
        }

        if (Objects.nonNull(source.getDadosAmortizacao())) {
            target.setDadosAmortizacao(dadosAmortizacaoConverter.from(source.getDadosAmortizacao()));
        }

        if (Objects.nonNull(source.getContaContabil()) && Objects.nonNull(source.getContaContabil().getId())) {
            target.setContaContabil(
                ContaContabilEntity
                    .builder()
                    .id(source.getContaContabil().getId())
                    .build()
            );
        }

        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(fornecedorConverter.from(source.getFornecedor()));
        }

        if(Objects.nonNull(source.getValorEntrada())){
            target.setValorDeEntrada(source.getValorEntrada());
        }

        return target;
    }

    @Override
    public Patrimonio to(PatrimonioEntity source) {
        Patrimonio target = super.to(source);

        if (Objects.nonNull(source.getDataAtivacao())) {
            target.setDataAtivacao(DateUtils.asLocalDateTime(source.getDataAtivacao()));
        }

        if (Objects.nonNull(source.getDataAquisicao())) {
            target.setDataAquisicao(DateUtils.asLocalDateTime(source.getDataAquisicao()));
        }

        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNotaLancamentoContabil(
                NotaLancamentoContabil
                    .builder()
                    .id(source.getNotaLancamentoContabil().getId())
                    .numero(source.getNotaLancamentoContabil().getNumero())
                    .dataLancamento(DateUtils.asLocalDateTime(source.getNotaLancamentoContabil().getDataLancamento()))
                    .build());
        }

        if (Objects.nonNull(source.getInicioVidaUtil())) {
            target.setInicioVidaUtil(DateUtils.asLocalDateTime(source.getInicioVidaUtil()));
        }

        if (Objects.nonNull(source.getDataVencimento())) {
            target.setDataVencimento(DateUtils.asLocalDateTime(source.getDataVencimento()));
        }

        if (Objects.nonNull(source.getFimVidaUtil())) {
            target.setFimVidaUtil(DateUtils.asLocalDateTime(source.getFimVidaUtil()));
        }

        if (Objects.nonNull(source.getDataFinalAtivacao())) {
            target.setDataFinalAtivacao(DateUtils.asLocalDateTime(source.getDataFinalAtivacao()));
        }

        if (Objects.nonNull(source.getDataCadastro())) {
            target.setDataCadastro(DateUtils.asLocalDateTime(source.getDataCadastro()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.to(source.getOrgao()));
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(unidadeOrganizacionalConverter.to(source.getSetor()));
        }

        if (Objects.nonNull(source.getDadosAmortizacao())) {
            target.setDadosAmortizacao(dadosAmortizacaoConverter.to(source.getDadosAmortizacao()));
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.to(source.getContaContabil()));
        }

        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(fornecedorConverter.to(source.getFornecedor()));
        }

        if(Objects.nonNull(source.getValorDeEntrada())){
            target.setValorEntrada(source.getValorDeEntrada());
        }

        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNotaLancamentoContabil(notaLancamentoContabilConverter.to(source.getNotaLancamentoContabil()));
        }

        return target;
    }
}
