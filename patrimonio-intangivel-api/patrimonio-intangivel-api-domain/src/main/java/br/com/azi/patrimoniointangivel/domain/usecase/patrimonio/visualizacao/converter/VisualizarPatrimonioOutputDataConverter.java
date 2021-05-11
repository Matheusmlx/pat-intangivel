package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.converter;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.VisualizarPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import br.com.azi.patrimoniointangivel.utils.validate.DateValidate;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
public class VisualizarPatrimonioOutputDataConverter extends GenericConverter<Patrimonio, VisualizarPatrimonioOutputData> {

    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Override
    public VisualizarPatrimonioOutputData to(Patrimonio source) {
        VisualizarPatrimonioOutputData target = super.to(source);

        VisualizarPatrimonioOutputDataConfigAmortizacaoConverter outputDataConfigAmortizacaoConverter = new VisualizarPatrimonioOutputDataConfigAmortizacaoConverter();

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(
                VisualizarPatrimonioOutputData.UnidadeOrganizacional
                    .builder()
                    .id(source.getOrgao().getId())
                    .sigla(source.getOrgao().getSigla())
                    .nome(source.getOrgao().getNome())
                    .build()
            );
        }
        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(
                VisualizarPatrimonioOutputData.UnidadeOrganizacional
                    .builder()
                    .id(source.getSetor().getId())
                    .sigla(source.getSetor().getSigla())
                    .nome(source.getSetor().getNome())
                    .build()
            );
        }
        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(source.getFornecedor().getId());
        }

        if (Objects.nonNull(source.getDadosAmortizacao())) {
            target.setConfigAmortizacao(outputDataConfigAmortizacaoConverter.to(source.getDadosAmortizacao().getConfigAmortizacao()));
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(
                VisualizarPatrimonioOutputData.ContaContabil
                    .builder()
                    .id(source.getContaContabil().getId())
                    .codigo(source.getContaContabil().getCodigo())
                    .descricao(source.getContaContabil().getDescricao())
                    .build()
            );
        }

        target.setDataAtivacao(Date.from(source.getDataAtivacao().atZone(ZoneId.systemDefault()).toInstant()));
        if (Objects.nonNull(source.getDataAquisicao())) {
            target.setDataAquisicao(Date.from(source.getDataAquisicao().atZone(ZoneId.systemDefault()).toInstant()));
        }
        if(Objects.nonNull(source.getNotaLancamentoContabil())){
            target.setNumeroNL(source.getNotaLancamentoContabil().getNumero());
            if(Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())){
                target.setDataNL(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }
        target.setInicioVidaUtil(Date.from(source.getInicioVidaUtil().atZone(ZoneId.systemDefault()).toInstant()));

        if (Objects.nonNull(source.getDataVencimento())) {
            target.setDataVencimento(Date.from(source.getDataVencimento().atZone(ZoneId.systemDefault()).toInstant()));
        }
        if (Objects.nonNull(source.getFimVidaUtil())) {
            target.setFimVidaUtil(Date.from(source.getFimVidaUtil().atZone(ZoneId.systemDefault()).toInstant()));
        }

        if (Objects.nonNull(source.getInicioVidaUtil()) && Objects.nonNull(source.getFimVidaUtil())) {
            Integer dia = retornaPeriodoVidaUtilEmDias(source.getInicioVidaUtil(), source.getFimVidaUtil());
            Integer mes = retornaPeriodoVidaUtilEmMeses(source.getInicioVidaUtil(), source.getFimVidaUtil());
            target.setPeriodoVidaUtil(VisualizarPatrimonioOutputData.Periodo.builder().dia(dia).mes(mes).build());
        }

        if (Objects.nonNull(target.getConfigAmortizacao())) {
            target.getConfigAmortizacao().setValorAmortizadoAcumulado(setaValorAmortizadoAcumulado(target));

            target.getConfigAmortizacao().setValorAmortizadoMensal(setaValorAmortizadoMensal(target));
        }

        return target;
    }

    private BigDecimal setaValorAmortizadoAcumulado(VisualizarPatrimonioOutputData target) {
        return target.getValorAquisicao().subtract(target.getValorLiquido());
    }

    private BigDecimal setaValorAmortizadoMensal(VisualizarPatrimonioOutputData target) {
        return amortizacaoDataProvider.buscarValorSubtraidoPorPatrimonioId(target.getId());
    }

    private int retornaPeriodoVidaUtilEmDias(LocalDateTime inicioVidaUtil, LocalDateTime fimVidaUtil) {

        @SuppressWarnings("empty-statement")
        LocalDate bday = inicioVidaUtil.toLocalDate();
        LocalDate today = fimVidaUtil.toLocalDate();
        Period age = Period.between(bday, today);

        return age.getDays();
    }

    private int retornaPeriodoVidaUtilEmMeses(LocalDateTime inicioVidaUtil, LocalDateTime fimVidaUtil) {

        @SuppressWarnings("empty-statement")
        LocalDate bday = inicioVidaUtil.toLocalDate();
        LocalDate today = fimVidaUtil.toLocalDate();
        Period age = Period.between(bday, today);
        Integer meses = age.getYears() * 12;
        return age.getMonths() + meses;
    }

    private static class VisualizarPatrimonioOutputDataConfigAmortizacaoConverter extends GenericConverter<ConfigAmortizacao, VisualizarPatrimonioOutputData.ConfigAmortizacao> {
    }
}
