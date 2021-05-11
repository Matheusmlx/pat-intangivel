package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticoxls;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.ConfigContaContabilDataProviderImpl;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticoxls.entity.RelatorioInventarioSintetico;
import br.com.azi.patrimoniointangivel.gateway.integration.utils.XLSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.MaskFormatter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class GerarInventarioSinteticoXLSUseCase {

    private static final String NOME_RELATORIO = "inventariosintetico";

    private static final String[] COLUNAS = {"Conta Contábil", "Total de Bens", "Valor de Entrada (R$)", "Amort. Mensal (R$)", "Amort. Acumulada (R$)", "Valor Líquido (R$)"};

    private static final int ALTURA_DA_LINHA = 40;

    private RelatorioInventarioSintetico relatorioInventarioSintetico = new RelatorioInventarioSintetico();

    private List<RelatorioInventarioSintetico.ContaContabil> contasContabeis = new ArrayList<>();

    private RelatorioInventarioSintetico.ContaContabil conta = new RelatorioInventarioSintetico.ContaContabil();

    @Autowired
    private ConfigContaContabilDataProviderImpl configContaContabilDataProvider;

    public Arquivo executar(HalReportIntegrationProperties integrationProperties, List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado, UnidadeOrganizacional orgaoRelatorio, LocalDateTime dataFinal) {

        RelatorioInventarioSintetico relatorio = converterDadosRelatorio(registros, lancamentosContabeisAgrupado, orgaoRelatorio, dataFinal);
        List<Map<Integer, Object>> dataSource = configurarDataSouce(integrationProperties, relatorio);
        XLSFactory xlsFactory = configurarRelatorio();

        return gerarRelatorio(xlsFactory, dataSource);
    }

    private RelatorioInventarioSintetico converterDadosRelatorio(List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupados, UnidadeOrganizacional orgaoRelatorio, LocalDateTime dataFinal) {
        inicializaRelatorio();

        List<ContaContabil> contasContabeisList = registros.stream().map(RelatorioSintetico::getContaContabil).distinct().collect(Collectors.toList());
        for (ContaContabil contaContabil : contasContabeisList) {
            List<RelatorioSintetico> registroPorContaContabil = registros.stream().filter(reg -> reg.getContaContabil().getCodigo().equals(contaContabil.getCodigo())).collect(Collectors.toList());
            setaTotalDeBens(registroPorContaContabil);
            setaValorAquisicao(registroPorContaContabil, lancamentosContabeisAgrupados);
            setaValorAmortizadoAcumulado(registroPorContaContabil);
            setaValorAmortizadoMensal(registroPorContaContabil, dataFinal);
            setaValorLiquido(registroPorContaContabil);
            setaNomeContaContabil(contaContabil);
            setaTipoContaContabil(contaContabil);

            criaContaContabilRelatorioSintetico(conta);
            conta = new RelatorioInventarioSintetico.ContaContabil();
            inicializaConta();
        }

        String nomeOrgao = retornaNomeOrgao(orgaoRelatorio);
        relatorioInventarioSintetico.setContasContabeis(contasContabeis);
        setaDadosGeraisRelatorioSintetico(nomeOrgao, dataFinal);
        ordenaContasPorCodigo(relatorioInventarioSintetico);

        return relatorioInventarioSintetico;
    }


    private void setaTotalDeBens(List<RelatorioSintetico> relatorio) {
        Long totalBens = relatorio.stream().map(RelatorioSintetico::getPatrimonioId).distinct().count();
        conta.setTotalDeBens(totalBens);
    }

    private void setaValorAquisicao(List<RelatorioSintetico> relatorio, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupados) {
        List<RelatorioSintetico> relatoriosDistintos = relatorio.stream().filter(distinctByKey(RelatorioSintetico::getPatrimonioId)).collect(Collectors.toList());

        relatoriosDistintos.forEach(relatorioDistindo -> {
            lancamentosContabeisAgrupados.forEach(lancamentosContabeisAgrupado -> {
                if (relatorioDistindo.getPatrimonioId().equals(lancamentosContabeisAgrupado.getPatrimonio().getId())) {
                    conta.setValorAquisicao(conta.getValorAquisicao().add(lancamentosContabeisAgrupado.getMaiorValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN));
                }
            });
        });
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private void setaValorAmortizadoAcumulado(List<RelatorioSintetico> relatorio) {
        for (RelatorioSintetico rel : relatorio) {
            if (Objects.nonNull(rel.getValorAmortizadoMensal())) {
                conta.setAmortizacaoAcumulada(conta.getAmortizacaoAcumulada().add(rel.getValorAmortizadoMensal(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN));
            }
        }
    }

    private void setaValorAmortizadoMensal(List<RelatorioSintetico> relatorio, LocalDateTime dataFinal) {
        for (RelatorioSintetico rel : relatorio) {
            if (Objects.nonNull(rel.getDataCadastro()) && Objects.nonNull(rel.getValorAmortizadoMensal())) {
                if (dataFinal.getMonthValue() == rel.getDataCadastro().getMonthValue() && dataFinal.getYear() == rel.getDataCadastro().getYear()) {
                    conta.setAmortizacaoMensal(conta.getAmortizacaoMensal().add(rel.getValorAmortizadoMensal(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN));
                } else {
                    conta.setAmortizacaoMensal(conta.getAmortizacaoMensal().add(BigDecimal.ZERO));
                }
            }
        }
    }

    private void setaValorLiquido(List<RelatorioSintetico> relatorio) {
        List<RelatorioSintetico> patrimoniosDistintos = relatorio.stream().filter(distinctByKey(RelatorioSintetico::getPatrimonioId)).collect(Collectors.toList());
        for (RelatorioSintetico patrimonioRel : patrimoniosDistintos) {
            if (Objects.isNull(patrimonioRel.getValorLiquido())) {
                conta.setValorLiquido(conta.getValorLiquido().add(patrimonioRel.getValorAquisicao(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN));
            } else {
                Optional<RelatorioSintetico> ultimoRegistro = encontraValorLiquidoMinimo(patrimonioRel, relatorio);
                ultimoRegistro.ifPresent(relatorioSintetico -> conta.setValorLiquido(conta.getValorLiquido().add(relatorioSintetico.getValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN)));
            }
        }
    }

    private Optional<RelatorioSintetico> encontraValorLiquidoMinimo(RelatorioSintetico patrimonioRel, List<RelatorioSintetico> patrimoniosDistintos) {
        List<RelatorioSintetico> sublist = new ArrayList<>();
        for (RelatorioSintetico registro : patrimoniosDistintos) {
            if (registro.getPatrimonioId().equals(patrimonioRel.getPatrimonioId())) {
                sublist.add(registro);
            }
        }
        return sublist.stream().min(Comparator.comparing(RelatorioSintetico::getValorLiquido));
    }

    private void setaNomeContaContabil(ContaContabil contaContabil) {
        conta.setNome(formatString(contaContabil.getCodigo(), "##.###.##.##") + " - " + contaContabil.getDescricao());
    }

    private void setaTipoContaContabil(ContaContabil contaContabil) {
        Optional<ConfigContaContabil> config = configContaContabilDataProvider.buscarAtualPorContaContabil(contaContabil.getId());
        config.ifPresent(conf -> conta.setTipo(conf.getTipo().toString()));
    }

    public static String formatString(String value, String pattern) {
        MaskFormatter mf;
        try {
            mf = new MaskFormatter(pattern);
            mf.setValueContainsLiteralCharacters(false);
            return mf.valueToString(value);
        } catch (ParseException ex) {
            return value;
        }
    }

    private String retornaNomeOrgao(UnidadeOrganizacional orgaoRelatorio) {
        return orgaoRelatorio.getNome() + " - " + orgaoRelatorio.getSigla();
    }

    private void inicializaRelatorio() {
        inicializaConta();
        contasContabeis = new ArrayList<>();
        relatorioInventarioSintetico.setTotalOrgaos(0L);
        relatorioInventarioSintetico.setTotalValorLiquido(BigDecimal.ZERO);
        relatorioInventarioSintetico.setTotalValorAquisicao(BigDecimal.ZERO);
        relatorioInventarioSintetico.setTotalAmortizacaoMensal(BigDecimal.ZERO);
        relatorioInventarioSintetico.setTotalAmortizacaoAcumulada(BigDecimal.ZERO);
    }

    private void inicializaConta() {
        conta.setTotalDeBens(0L);
        conta.setValorLiquido(BigDecimal.ZERO);
        conta.setValorAquisicao(BigDecimal.ZERO);
        conta.setAmortizacaoMensal(BigDecimal.ZERO);
        conta.setAmortizacaoAcumulada(BigDecimal.ZERO);
    }

    private void setaDadosGeraisRelatorioSintetico(String nomeOrgao, LocalDateTime dataFinal) {
        for (RelatorioInventarioSintetico.ContaContabil conta : relatorioInventarioSintetico.getContasContabeis()) {
            relatorioInventarioSintetico.setTotalValorAquisicao(relatorioInventarioSintetico.getTotalValorAquisicao().add(conta.getValorAquisicao()));
            relatorioInventarioSintetico.setTotalAmortizacaoMensal(relatorioInventarioSintetico.getTotalAmortizacaoMensal().add(conta.getAmortizacaoMensal()));
            relatorioInventarioSintetico.setTotalAmortizacaoAcumulada(relatorioInventarioSintetico.getTotalAmortizacaoAcumulada().add(conta.getAmortizacaoAcumulada()));
            relatorioInventarioSintetico.setTotalValorLiquido(relatorioInventarioSintetico.getTotalValorLiquido().add(conta.getValorLiquido()));
            relatorioInventarioSintetico.setTotalOrgaos(relatorioInventarioSintetico.getTotalOrgaos() + conta.getTotalDeBens());
        }
        relatorioInventarioSintetico.setNomeOrgao(nomeOrgao);
        relatorioInventarioSintetico.setDataRelatorio(criaDataRelatorio(dataFinal));
    }

    private void ordenaContasPorCodigo(RelatorioInventarioSintetico relatorio) {
        Collections.sort(relatorio.getContasContabeis(), Comparator.comparing(RelatorioInventarioSintetico.ContaContabil::getNome));
        relatorioInventarioSintetico.setContasContabeis(relatorio.getContasContabeis());
    }

    private String criaDataRelatorio(LocalDateTime dataFinal) {
        String mes = dataFinal.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt"));
        String ano = String.valueOf(dataFinal.getYear());
        return mes + "/" + ano;
    }

    private void criaContaContabilRelatorioSintetico(RelatorioInventarioSintetico.ContaContabil conta) {
        contasContabeis.add(RelatorioInventarioSintetico.ContaContabil
            .builder()
            .nome(conta.getNome())
            .tipo(conta.getTipo())
            .valorAquisicao(conta.getValorAquisicao())
            .amortizacaoMensal(conta.getAmortizacaoMensal())
            .amortizacaoAcumulada(conta.getAmortizacaoAcumulada())
            .valorLiquido(conta.getValorLiquido())
            .totalDeBens(conta.getTotalDeBens())
            .build());
    }

    private List<Map<Integer, Object>> configurarDataSouce(HalReportIntegrationProperties integrationProperties, RelatorioInventarioSintetico relatorio) {
        List<Map<Integer, Object>> dados = new ArrayList<>();
        relatorio.getContasContabeis().forEach(conta -> {
            dados.add(getMapItem(conta));
        });

        return dados;
    }

    private Map<Integer, Object> getMapItem(RelatorioInventarioSintetico.ContaContabil conta) {
        Map<Integer, Object> item = new HashMap<>();

        NumberFormat df = NumberFormat.getCurrencyInstance();


        item.put(0, conta.getNome());
        item.put(1, conta.getTotalDeBens().toString());
        item.put(2, conta.getValorAquisicao().doubleValue());
        item.put(3, conta.getTipo().equals("AMORTIZAVEL") || (!conta.getTipo().equals("AMORTIZAVEL") && !df.format(conta.getAmortizacaoMensal()).equals("R$ 0,00")) ? conta.getAmortizacaoMensal().doubleValue() : "-");
        item.put(4, conta.getTipo().equals("AMORTIZAVEL") || (!conta.getTipo().equals("AMORTIZAVEL") && !df.format(conta.getAmortizacaoAcumulada()).equals("R$ 0,00")) ? conta.getAmortizacaoAcumulada().doubleValue() : "-");
        item.put(5, conta.getValorLiquido().doubleValue());

        return item;
    }

    private XLSFactory configurarRelatorio() {
        return XLSFactory.getInstance(COLUNAS.length, "dd/MM/yyyy")
            .inserirTituloLinha(relatorioInventarioSintetico.getNomeOrgao())
            .inserirCabecalhos(COLUNAS);
    }

    private Arquivo gerarRelatorio(XLSFactory xlsFactory, List<Map<Integer, Object>> dados) {

        ArrayList<String> typesCells = setTypesCells();

        try {
            xlsFactory = xlsFactory.inserirDadosNasCelulas(dados, typesCells)
                .autoSizeColumns(COLUNAS.length)
                .setSizeColumn(0, 15000)
                .setSizeColumn(1, 7000)
                .setSizeColumn(2, 8000)
                .setSizeColumn(3, 7000)
                .setSizeColumn(4, 10000)
                .setSizeColumn(5, 7000)
                .autoSizeRows(dados.size(), ALTURA_DA_LINHA);

            byte[] content = xlsFactory.build();

            return Arquivo
                .builder()
                .contentType("application/x-msexcel")
                .nome(String.format("%s.pdf", NOME_RELATORIO))
                .content(content)
                .build();
        } catch (Exception e) {
            throw new RelatorioException("Não foi possivel realizar a geração do relatorio.", e);
        }
    }

    private ArrayList<String> setTypesCells() {
        ArrayList<String> cellsTypes = new ArrayList<>();
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("R$#,##0.00");
        cellsTypes.add("R$#,##0.00");
        cellsTypes.add("R$#,##0.00");
        cellsTypes.add("R$#,##0.00");
        return cellsTypes;
    }
}
