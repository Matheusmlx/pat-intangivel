package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico;

import br.com.azi.patrimoniointangivel.domain.constant.enums.TipoRelatorio;
import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Job;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioAnalitico;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception.SelecionarTipoAmortizacaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.converter.GerarInventarioAnaliticoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.exception.AmortizacaoEmAndamentoException;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.analitico.exception.LancamentosContabeisException;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception.BuscaRelatorioException;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static br.com.azi.patrimoniointangivel.utils.string.StringUtils.formatString;

@AllArgsConstructor
public class GerarInventarioAnaliticoUseCaseImpl implements GerarInventarioAnaliticoUseCase {

    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private AmortizacaoDataProvider amortizacaoDataProvider;

    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    private GerarInventarioAnaliticoOutputDataConverter outputDataConverter;

    @Override
    public GerarInventarioAnaliticoOutputData executar(GerarInventarioAnaliticoInputData inputData) {
        validarSeAmortizacaoEmAndamento();
        validarDadosEntrada(inputData);

        GerarInventarioAnaliticoUseCaseHelper gerarRelatorioHelper = new GerarInventarioAnaliticoUseCaseHelper(configContaContabilDataProvider);

        LocalDateTime dataReferencia = gerarRelatorioHelper.montarDataReferenciaRelatorio(inputData);
        LocalDateTime dataFinal = gerarRelatorioHelper.calcularDataFinal(dataReferencia);

        List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado = buscarLancamentosContabeisAgrupados(dataReferencia, inputData.getOrgao());
        validarSeExistemRegistrosInventario(lancamentosContabeisAgrupado);

        List<RelatorioAnalitico.ContaContabilIn> itensContasContabeis =   prepararContasContabeisParaInventario(lancamentosContabeisAgrupado);
        separarPatrimoniosDistintosPorConta(itensContasContabeis);

        encontrarDadosPatrimonioParaDataReferencia(itensContasContabeis, dataReferencia, inputData.getOrgao());
        atualizaTotalizadoresPorContaContabil(itensContasContabeis, dataFinal);

        RelatorioAnalitico relatorioAnalitico = criaInventarioAnalitico(itensContasContabeis, lancamentosContabeisAgrupado, dataFinal);

        Arquivo arquivo = gerarArquivoRelatorio(inputData, relatorioAnalitico);

        return outputDataConverter.to(arquivo);
    }

    private void validarSeAmortizacaoEmAndamento(){
        Job job = Job.getInstance();
        if (job.getStatus().equals(Job.Status.EM_ANDAMENTO)){
            throw new AmortizacaoEmAndamentoException();
        }
    }

    private void validarDadosEntrada(GerarInventarioAnaliticoInputData inputData){
        Validator.of(inputData)
            .validate(GerarInventarioAnaliticoInputData::getFormato, Objects::nonNull, "O formato para impressão é nulo.")
            .validate(GerarInventarioAnaliticoInputData::getOrgao, Objects::nonNull, "O id do órgão é nulo.")
            .validate(GerarInventarioAnaliticoInputData::getMesReferencia, Objects::nonNull, "O mês de referência é nulo.")
            .get();
    }

    private List<LancamentosContabeisAgrupado> buscarLancamentosContabeisAgrupados(LocalDateTime dataFinal, Long orgao) {
        List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupados = lancamentosContabeisDataProvider.buscarLancamentosContabeisAgrupadosPorOrgao(dataFinal, orgao);
        return validarSeUltimoLancamentoContabilCredito(lancamentosContabeisAgrupados, orgao, dataFinal);
    }

    private void validarSeExistemRegistrosInventario(List<LancamentosContabeisAgrupado> relatorio) {
        if(relatorio.isEmpty()){
            throw new BuscaRelatorioException();
        }
    }

    List<RelatorioAnalitico.ContaContabilIn> prepararContasContabeisParaInventario(List<LancamentosContabeisAgrupado> registros) {
        List<ContaContabil> contasContabeis =  encontrarContasContabeis(registros);
        List<RelatorioAnalitico.ContaContabilIn> contasContabeisInventario = new ArrayList<>();
        for (ContaContabil contaContabil: contasContabeis) {
            contasContabeisInventario.add(
                RelatorioAnalitico.ContaContabilIn
                    .builder()
                    .id(contaContabil.getId())
                    .descricao(contaContabil.getDescricao())
                    .codigo(contaContabil.getCodigo())
                    .amortizavel(validarSeContaContabilAmortizavel(contaContabil))
                    .patrimonios(converterPatrimoniosParaRelatorio(encontrarPatrimoniosPorContaContabil(registros, contaContabil)))
                    .build());
        }
        return contasContabeisInventario;
    }

    private void separarPatrimoniosDistintosPorConta(List<RelatorioAnalitico.ContaContabilIn> itensContasContabeis) {
        for (RelatorioAnalitico.ContaContabilIn contaContabil : itensContasContabeis) {
            contaContabil.setPatrimonios(encontrarPatrimoniosDistintos(contaContabil.getPatrimonios()));
        }
    }

    private void encontrarDadosPatrimonioParaDataReferencia(List<RelatorioAnalitico.ContaContabilIn> itensContasContabeis, LocalDateTime dataFinal, Long orgao) {
        for (RelatorioAnalitico.ContaContabilIn contaContabil: itensContasContabeis) {
            for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
                setarDadosPatrimonio(patrimonio, dataFinal, orgao);
            }
        }
    }

    private List<LancamentosContabeisAgrupado> validarSeUltimoLancamentoContabilCredito(List<LancamentosContabeisAgrupado> patrimoniosCreditadosNoOrgaoAteData, Long orgao, LocalDateTime dataFinal) {
        List<LancamentosContabeisAgrupado> patrimoniosDebitadosNoOrgaoAteData = new ArrayList<>();
        for (LancamentosContabeisAgrupado lancamentoContabil: patrimoniosCreditadosNoOrgaoAteData) {
            if (lancamentosContabeisDataProvider.validarSeUltimoLancamentoNoOrgaoCredito(lancamentoContabil.getPatrimonio().getId(), orgao, dataFinal)) {
                patrimoniosDebitadosNoOrgaoAteData.add(lancamentoContabil);
            }
        }
        patrimoniosCreditadosNoOrgaoAteData.removeAll(patrimoniosDebitadosNoOrgaoAteData);
        return patrimoniosCreditadosNoOrgaoAteData;
    }

    private List<RelatorioAnalitico.Patrimonio> converterPatrimoniosParaRelatorio(List<LancamentosContabeisAgrupado> patrimonios) {
        List<RelatorioAnalitico.Patrimonio> registrosPatrimonios = new ArrayList<>();
        for (LancamentosContabeisAgrupado patrimonio: patrimonios) {
            registrosPatrimonios.add(
                RelatorioAnalitico.Patrimonio
                    .builder()
                    .id(patrimonio.getPatrimonio().getId())
                    .numero(patrimonio.getPatrimonio().getNumero())
                    .nome(patrimonio.getPatrimonio().getNome())
                    .tipo(patrimonio.getPatrimonio().getTipo().getValor())
                    .amortizavel(patrimonio.getPatrimonio().getAmortizavel())
                    .valorAquisicao(patrimonio.getMaiorValorLiquido())
                    .build());
        }
        return registrosPatrimonios;
    }

    private List<LancamentosContabeisAgrupado> encontrarPatrimoniosPorContaContabil(List<LancamentosContabeisAgrupado> registros, ContaContabil contaContabil) {
        List<LancamentosContabeisAgrupado>  lancamentosContabeis = new ArrayList<>();
        for (LancamentosContabeisAgrupado lancamentoContabil: registros) {
            if(lancamentoContabil.getPatrimonio().getContaContabil().getId().equals(contaContabil.getId())) {
                lancamentosContabeis.add(lancamentoContabil);
            }
        }
        return lancamentosContabeis;
    }

    private Boolean validarSeContaContabilAmortizavel(ContaContabil contaContabil) {
        ConfigContaContabil configContaContabil = buscaConfigContaContabil(contaContabil);
        return configContaContabil.getTipo().equals(ConfigContaContabil.Tipo.AMORTIZAVEL);
    }

    private ConfigContaContabil buscaConfigContaContabil(ContaContabil contaContabil) {
        Optional<ConfigContaContabil> configContaContabil = configContaContabilDataProvider.buscarAtualPorContaContabil(contaContabil.getId());
        return configContaContabil.orElseThrow(SelecionarTipoAmortizacaoException::new);
    }


    private List<ContaContabil> encontrarContasContabeis(List<LancamentosContabeisAgrupado> registros) {
        List<ContaContabil> contasContabeis = new ArrayList<>();
        for (LancamentosContabeisAgrupado lancamentoContabil: registros) {
            if (!contasContabeis.contains(lancamentoContabil.getPatrimonio().getContaContabil())){
                contasContabeis.add(lancamentoContabil.getPatrimonio().getContaContabil());
            }
        }
        return contasContabeis;
    }

    private RelatorioAnalitico criaInventarioAnalitico(List<RelatorioAnalitico.ContaContabilIn> contasContabeis, List<LancamentosContabeisAgrupado> registros, LocalDateTime data){
        return RelatorioAnalitico
            .builder()
            .dataRelatorio(data)
            .contasContabeis(contasContabeis)
            .orgao(RelatorioAnalitico.Orgao
                .builder()
                .nome(registros.get(0).getOrgao().getNome())
                .sigla(registros.get(0).getOrgao().getSigla())
                .totalAmortizacaoAcumulada(calcularAmortizacaoAcumuladaPorOrgao(contasContabeis))
                .totalAmortizacaoMensal(calcularAmortizacaoMensalPorOrgao(contasContabeis, data))
                .totalValorAquisicao(calcularValorAquisicaoPorOrgao(contasContabeis))
                .totalValorLiquido(calcularValorLiquidoPorOrgao(contasContabeis))
                .totalDeBens(calculaTotalDeBensPorOrgao(contasContabeis))
                .build())
            .build();
    }

    private Arquivo gerarArquivoRelatorio(GerarInventarioAnaliticoInputData inputData, RelatorioAnalitico relatorioAnalitico) {
        if (inputData.getFormato().equals(TipoRelatorio.XLS.name())) {
            return sistemaDeRelatoriosIntegration.gerarRelatorioInventarioAnaliticoXLS(relatorioAnalitico);
        }
        return sistemaDeRelatoriosIntegration.gerarRelatorioInventarioAnaliticoPDF(relatorioAnalitico);
    }

    private void setarDadosPatrimonio(RelatorioAnalitico.Patrimonio patrimonio, LocalDateTime dataReferencia, Long orgao) {
        if(patrimonio.getAmortizavel() && existeAmortizacaoPorPatrimonio(patrimonio, dataReferencia)){
            patrimonio.setValorLiquido(buscarValorLiquidoPatrimonioNaData(patrimonio, dataReferencia));
            patrimonio.setValorAquisicao(buscarValorEntradaPatrimonioNaData(patrimonio, orgao, dataReferencia));
            patrimonio.setValorAmortizadoMensal(buscarValorAmortizadoPatrimonioNoMes(patrimonio, dataReferencia));
            patrimonio.setValorAmortizadoAcumulado(patrimonio.getValorAquisicao().subtract(patrimonio.getValorLiquido()));
        }else {
            patrimonio.setValorLiquido(patrimonio.getValorAquisicao());
            patrimonio.setValorAquisicao(patrimonio.getValorAquisicao());
            patrimonio.setValorAmortizadoAcumulado(BigDecimal.ZERO);
            patrimonio.setValorAmortizadoMensal(BigDecimal.ZERO);
        }
    }

    private void atualizaTotalizadoresPorContaContabil(List<RelatorioAnalitico.ContaContabilIn> contasContabeis, LocalDateTime dataFinal) {
        for (RelatorioAnalitico.ContaContabilIn contaContabil: contasContabeis) {
            contaContabil.setDescricao(formatString(contaContabil.getCodigo(),"##.###.##.##") + " - " + contaContabil.getDescricao());
            contaContabil.setTotalDeBens(contaContabil.getPatrimonios().size());
            contaContabil.setAmortizacaoAcumulada(calcularAmortizacaoAcumuladaPorContaContabil(contaContabil));
            if (mostrarAmortizacaoMensal(dataFinal)) {
                contaContabil.setAmortizacaoMensal(calcularAmortizacaoMensalPorContaContabil(contaContabil));
            }else {
                contaContabil.setAmortizacaoMensal(BigDecimal.ZERO);
            }
            contaContabil.setValorAquisicao(calcularValorAquisicaoPorContaContabil(contaContabil));
            contaContabil.setValorLiquido(calcularValorLiquidoPorContaContabil(contaContabil));
        }
    }

    private int calculaTotalDeBensPorOrgao(List<RelatorioAnalitico.ContaContabilIn> contasContabeis) {
        int totalDeBens = 0;
        for (RelatorioAnalitico.ContaContabilIn contaContabil: contasContabeis) {
            totalDeBens += contaContabil.getPatrimonios().size();
        }
        return totalDeBens;
    }

    private Boolean mostrarAmortizacaoMensal(LocalDateTime mesReferencia) {
        return !DateUtils.mesmoMesAno(mesReferencia, LocalDateTime.now());
    }

    private BigDecimal calcularAmortizacaoAcumuladaPorOrgao(List<RelatorioAnalitico.ContaContabilIn> contasContabeis) {
        BigDecimal amortizacaoAcumuladaTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        for(RelatorioAnalitico.ContaContabilIn contaContabil: contasContabeis) {
            for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
                if (patrimonio.getAmortizavel()){
                    amortizacaoAcumuladaTotal = amortizacaoAcumuladaTotal.add(patrimonio.getValorAmortizadoAcumulado(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
                }
            }
        }
        return amortizacaoAcumuladaTotal;
    }

    private BigDecimal calcularAmortizacaoMensalPorOrgao(List<RelatorioAnalitico.ContaContabilIn> contasContabeis, LocalDateTime dataFinal) {
        BigDecimal amortizacaoMensalTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        if (mostrarAmortizacaoMensal(dataFinal)) {
            for(RelatorioAnalitico.ContaContabilIn contaContabil: contasContabeis) {
                for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
                    if(patrimonio.getAmortizavel()){
                        amortizacaoMensalTotal = amortizacaoMensalTotal.add(patrimonio.getValorAmortizadoMensal(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
                    }
                }
            }
        }
        return amortizacaoMensalTotal;
    }

    private BigDecimal calcularValorAquisicaoPorOrgao(List<RelatorioAnalitico.ContaContabilIn> contasContabeis) {
        BigDecimal valorAquisicaoTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        for(RelatorioAnalitico.ContaContabilIn contaContabil: contasContabeis) {
            for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
                valorAquisicaoTotal = valorAquisicaoTotal.add(patrimonio.getValorAquisicao(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
            }
        }
        return valorAquisicaoTotal;
    }

    private BigDecimal calcularValorLiquidoPorOrgao(List<RelatorioAnalitico.ContaContabilIn> contasContabeis) {
        BigDecimal valorLiquidoTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        for(RelatorioAnalitico.ContaContabilIn contaContabil: contasContabeis) {
            for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
                valorLiquidoTotal = valorLiquidoTotal.add(patrimonio.getValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
            }
        }
        return valorLiquidoTotal;
    }

    private BigDecimal calcularAmortizacaoAcumuladaPorContaContabil(RelatorioAnalitico.ContaContabilIn contaContabil) {
        BigDecimal amortizacaoAcumuladaTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
            if(patrimonio.getAmortizavel()) {
                amortizacaoAcumuladaTotal = amortizacaoAcumuladaTotal.add(patrimonio.getValorAmortizadoAcumulado(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
            }
        }
        return amortizacaoAcumuladaTotal;
    }

    private BigDecimal calcularAmortizacaoMensalPorContaContabil(RelatorioAnalitico.ContaContabilIn contaContabil) {
        BigDecimal amortizacaoMensalTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
            if(patrimonio.getAmortizavel()){
                amortizacaoMensalTotal = amortizacaoMensalTotal.add(patrimonio.getValorAmortizadoMensal(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
            }
        }
        return amortizacaoMensalTotal;
    }

    private BigDecimal calcularValorAquisicaoPorContaContabil(RelatorioAnalitico.ContaContabilIn contaContabil) {
        BigDecimal valorAquisicaoTotal =  new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
            valorAquisicaoTotal = valorAquisicaoTotal.add(patrimonio.getValorAquisicao()).setScale(2, RoundingMode.HALF_EVEN);
        }
        return valorAquisicaoTotal;
    }

    private BigDecimal calcularValorLiquidoPorContaContabil(RelatorioAnalitico.ContaContabilIn contaContabil) {
        BigDecimal valorLiquidoTotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
        for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
            valorLiquidoTotal = valorLiquidoTotal.add(patrimonio.getValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return valorLiquidoTotal;
    }


    private List<RelatorioAnalitico.Patrimonio> encontrarPatrimoniosDistintos(List<RelatorioAnalitico.Patrimonio> patrimonios) {
        return patrimonios.stream().filter( encontrarPatrimonioDistintoPorChave(RelatorioAnalitico.Patrimonio::getId)).collect(Collectors.toList());
    }

    private BigDecimal buscarValorAmortizadoPatrimonioNoMes(RelatorioAnalitico.Patrimonio patrimonio, LocalDateTime dataFinal) {
        if (existeAmortizacaoPorPatrimonioNoMes(patrimonio, dataFinal)){
            Amortizacao ultimaAmortizacaoPeriodo = buscarAmortizacaoPatrimonioNaData(patrimonio, dataFinal);
            return ultimaAmortizacaoPeriodo.getValorSubtraido();
        }
        return BigDecimal.ZERO;
    }

    private Boolean existeAmortizacaoPorPatrimonio(RelatorioAnalitico.Patrimonio patrimonio,LocalDateTime dataFinal) {
        return amortizacaoDataProvider.existePorPatrimonioAteDataLimite(patrimonio.getId(), dataFinal);
    }

    private Boolean existeAmortizacaoPorPatrimonioNoMes(RelatorioAnalitico.Patrimonio patrimonio,LocalDateTime dataFinal) {
        return amortizacaoDataProvider.existePorPatrimonioNoPeriodo(patrimonio.getId(), dataFinal);
    }

    private Amortizacao buscarAmortizacaoPatrimonioNaData(RelatorioAnalitico.Patrimonio patrimonio, LocalDateTime dataFinal) {
        Optional<Amortizacao> amortizacao = amortizacaoDataProvider.buscarPorPatrimonioEDataLimite(patrimonio.getId(), dataFinal);
        return (amortizacao.orElseThrow(PatrimonioNaoEncontradoException::new));
    }

    private BigDecimal buscarValorLiquidoPatrimonioNaData(RelatorioAnalitico.Patrimonio patrimonio, LocalDateTime dataFinal) {
        Amortizacao ultimaAmortizacaoPeriodo = buscarAmortizacaoPatrimonioNaData(patrimonio, dataFinal);
        return ultimaAmortizacaoPeriodo.getValorPosterior();
    }

    private LancamentosContabeis buscarUltimoLancamentoContabilAteDataReferencia(Long patrimonio, Long orgao, LocalDateTime dataReferencia) {
        Optional<LancamentosContabeis> lancamentoContabil = lancamentosContabeisDataProvider.buscarUltimoPorPatrimonioNoOrgaoAteDataReferencia(patrimonio, orgao, dataReferencia);
        return lancamentoContabil.orElseThrow(LancamentosContabeisException::new);
    }

    private BigDecimal buscarValorEntradaPatrimonioNaData(RelatorioAnalitico.Patrimonio patrimonio, Long orgao, LocalDateTime dataFinal) {
        LancamentosContabeis ultimoLancamentoContabil = buscarUltimoLancamentoContabilAteDataReferencia(patrimonio.getId(), orgao, dataFinal);
        return ultimoLancamentoContabil.getValorLiquido();
    }

    public static <T> Predicate<T> encontrarPatrimonioDistintoPorChave(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
