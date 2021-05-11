package br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.PeriodoAmortizacao;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.exception.RodarAmortizacaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.converter.IniciaAmortizacaoManualOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.exception.AmortizacaoManualException;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.salvadadosamortizacao.SalvaDadosAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class IniciaAmortizacaoManualUseCaseImpl implements IniciaAmortizacaoManualUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    private SalvaDadosAmortizacaoUseCase salvaDadosAmortizacaoUseCase;

    private AmortizacaoDataProvider amortizacaoDataProvider;

    private Clock clock;

    private IniciaAmortizacaoManualOutputDataConverter outputDataConverter;

    @Override
    public IniciaAmortizacaoManualOutputData executar(IniciaAmortizacaoManualInputData inputData) {
        List<Patrimonio> patrimonios = buscarPatrimoniosAtivos(inputData);

        PeriodoAmortizacao periodo = calcularPeriodoAmortizacaoManual(inputData);
        verificarSeMesCorrente(periodo);

        List<Patrimonio> patrimoniosAmortizados = amortizarPatrimonios(patrimonios, periodo);
        verificaSeTemPatrimoniosRetorno(patrimoniosAmortizados);

        return outputDataConverter.to(patrimoniosAmortizados);
    }

    private List<Patrimonio> buscarPatrimoniosAtivos(IniciaAmortizacaoManualInputData inputData){
        return patrimonioDataProvider.buscarPatrimoniosAmortizaveisPorOrgao(inputData.getOrgao());
    }

    private PeriodoAmortizacao calcularPeriodoAmortizacaoManual(IniciaAmortizacaoManualInputData inputData){
        PeriodoAmortizacao periodo = new PeriodoAmortizacao();
        periodo.setDataInicial(LocalDate.of(Integer.parseInt(inputData.getAno()), Integer.parseInt(inputData.getMes()), 1));
        periodo.setDataFinal(YearMonth.from(periodo.getDataInicial()).atEndOfMonth());
        return periodo;
    }

    private void verificarSeMesCorrente(PeriodoAmortizacao periodo) {
        if (DateUtils.mesmoMesAno(periodo.getDataInicial(), LocalDate.now(clock))) {
            throw new AmortizacaoManualException("Não é possível rodar amortizações para o mês corrente.");
        }
    }

    private List<Patrimonio> amortizarPatrimonios(List<Patrimonio> patrimonios, PeriodoAmortizacao mesReferencia) {
        List<Patrimonio> patrimoniosAmortizados = new ArrayList<>();
        for (Patrimonio patrimonio : patrimonios) {
            PeriodoAmortizacao periodo = calcularPeriodoAmortizacao(patrimonio, mesReferencia);
            if (verificaPeriodoValido(periodo) && verificaAcumulado(periodo)) {
                for (PeriodoAmortizacao mes: separaPeriodoEmMeses(periodo)) {
                    if (verificaQuotasContantes(patrimonio) && !verificaSeJaAmortizouNoPeriodo(patrimonio, mes) && !verificaDataAtivacao(patrimonio, mes)) {
                        if (!patrimoniosAmortizados.contains(patrimonio)) {
                            patrimoniosAmortizados.add(patrimonio);
                        }
                        salvaAmortizacaoQuotasConstantes(patrimonio, mes);
                    }
                }
            }else {
                if (verificaPeriodoValido(periodo) && verificaQuotasContantes(patrimonio) && !verificaSeJaAmortizouNoPeriodo(patrimonio, periodo)) {
                    if (!patrimoniosAmortizados.contains(patrimonio)) {
                        patrimoniosAmortizados.add(patrimonio);
                    }
                    salvaAmortizacaoQuotasConstantes(patrimonio, periodo);
                }
            }
        }
        return patrimoniosAmortizados;
    }

    private void verificaSeTemPatrimoniosRetorno(List<Patrimonio> patrimonios) {
        if (patrimonios.isEmpty()) {
            throw new AmortizacaoManualException("Não há amortizações pendentes no período");
        }
    }

    private Boolean verificaQuotasContantes(Patrimonio patrimonio){
        DadosAmortizacao dadosAmortizacao = buscarVinculoAmortizacao(patrimonio);
        ConfigAmortizacao configAmortizacao = buscar(dadosAmortizacao);
        return configAmortizacao.getMetodo().equals(ConfigAmortizacao.Metodo.QUOTAS_CONSTANTES);
    }

    private DadosAmortizacao buscarVinculoAmortizacao(Patrimonio patrimonio){
        Optional<DadosAmortizacao> dadosAmortizacaoEncontrados = dadosAmortizacaoDataProvider.buscarPorId(patrimonio.getDadosAmortizacao().getId());
        return dadosAmortizacaoEncontrados.orElseThrow(RodarAmortizacaoException::new);
    }

    private ConfigAmortizacao buscar(DadosAmortizacao dadosAmortizacao){
        Optional<ConfigAmortizacao> configAmortizacao = configAmortizacaoDataProvider.buscarPorId(dadosAmortizacao.getConfigAmortizacao().getId());
        return configAmortizacao.orElseThrow(RodarAmortizacaoException::new);
    }

    private Boolean verificaDataAtivacao(Patrimonio patrimonio, PeriodoAmortizacao periodo){
        return DateUtils.mesAnterior( periodo.getDataInicial(), patrimonio.getDataAtivacao());
    }

    private void salvaAmortizacaoQuotasConstantes(Patrimonio patrimonio, PeriodoAmortizacao periodo) {

        SalvaDadosAmortizacaoInputData inputData = new SalvaDadosAmortizacaoInputData(patrimonio.getId(),
            periodo.getDataInicial().atStartOfDay(),
            periodo.getDataFinal().atTime(23, 59, 59));
        salvaDadosAmortizacaoUseCase.executar(inputData);
    }

    private Boolean verificaPeriodoValido(PeriodoAmortizacao periodo) {
        return periodo.getDataFinal().isAfter(periodo.getDataInicial());
    }

    private Boolean verificaAcumulado(PeriodoAmortizacao periodo) {
        return  DateUtils.totalMeses(periodo.getDataInicial().withDayOfMonth(1), YearMonth.from(periodo.getDataFinal()).atEndOfMonth()) > 1;
    }

    private Boolean verificaSeJaAmortizouNoPeriodo(Patrimonio patrimonio, PeriodoAmortizacao periodo){
        return  amortizacaoDataProvider.existePorPatrimonioNoPeriodo(patrimonio.getId(), periodo.getDataInicial().atStartOfDay());
    }

    private List<PeriodoAmortizacao> separaPeriodoEmMeses(PeriodoAmortizacao periodo) {
        int meses = DateUtils.totalMeses(periodo.getDataInicial(),
            YearMonth.from(periodo.getDataFinal()).atEndOfMonth());
        List<PeriodoAmortizacao> intervalos = new ArrayList<>();

        intervalos.add(PeriodoAmortizacao
            .builder()
            .dataInicial(periodo.getDataInicial())
            .dataFinal(YearMonth.from(periodo.getDataInicial()).atEndOfMonth())
            .build());

        for (int i = 1; i < meses; i++) {
            LocalDate mesCorrente = periodo.getDataInicial().plusMonths(i);
            intervalos.add(PeriodoAmortizacao
                .builder()
                .dataInicial(LocalDate .now(clock) .withDayOfMonth(1) .withMonth(mesCorrente.getMonth().getValue()) .withYear(mesCorrente.getYear()))
                .dataFinal(YearMonth.from(LocalDate .now(clock) .withDayOfMonth(1) .withMonth(mesCorrente.getMonth().getValue()) .withYear(mesCorrente.getYear())).atEndOfMonth())
                .build());
        }

        intervalos.add(PeriodoAmortizacao
            .builder()
            .dataInicial(periodo.getDataFinal().withDayOfMonth(1))
            .dataFinal(periodo.getDataFinal())
            .build());

        return intervalos;
    }


    private PeriodoAmortizacao calcularPeriodoAmortizacao(Patrimonio patrimonio, PeriodoAmortizacao mesReferencia) {
        PeriodoAmortizacao periodo = new PeriodoAmortizacao();
        if (unicaAmortizacao(patrimonio) &&  !verificaDataAtivacao(patrimonio, mesReferencia)) {
            periodo.setDataInicial(patrimonio.getDataAtivacao().toLocalDate());
            if(mesReferencia.getDataFinal().isBefore(patrimonio.getFimVidaUtil().toLocalDate())){
                periodo.setDataFinal(mesReferencia.getDataFinal());
            }else{
                periodo.setDataFinal(patrimonio.getFimVidaUtil().toLocalDate());
            }
        } else{
            if (primeiraAmortizacao(patrimonio)) {
                periodo.setDataInicial(patrimonio.getDataAtivacao().toLocalDate());
                periodo.setDataFinal(mesReferencia.getDataFinal());
            } else {
                Amortizacao amortizacao = buscaUltimaAmortizacao(patrimonio);
                periodo.setDataInicial(amortizacao.getDataFinal().plusDays(1).toLocalDate());
                periodo.setDataFinal(mesReferencia.getDataFinal());
            }
        }
        return periodo;
    }

    private Amortizacao buscaUltimaAmortizacao(Patrimonio patrimonio) {
        Optional<Amortizacao> ultimaAmortizacao = amortizacaoDataProvider.buscarUltimaPorPatrimonio(patrimonio.getId());
        return ultimaAmortizacao.orElseThrow(RodarAmortizacaoException::new);
    }

    private Boolean primeiraAmortizacao(Patrimonio patrimonio) {
        return !amortizacaoDataProvider.existePorPatrimonio(patrimonio.getId());
    }

    private Boolean unicaAmortizacao(Patrimonio patrimonio) {
        return patrimonio.getFimVidaUtil().isBefore(LocalDateTime.now(clock))  && primeiraAmortizacao(patrimonio) ;
    }

}
