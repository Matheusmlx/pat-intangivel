package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticopdf;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ProdutoAtributoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.ConfigContaContabilDataProviderImpl;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.ConfiguracaoRelatorio;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticopdf.entity.RelatorioInventarioSintetico;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.utils.ReportHTMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import static br.com.azi.patrimoniointangivel.utils.string.StringUtils.formatString;

@Component
public class GerarInventarioSinteticoPDFUseCase {

    private static final String NOME_RELATORIO = "inventariosintetico";

    private static final String LOGO_ATRIBUTO = "logoRelatorio";

    @Autowired
    private ReportHTMLUtils reportHTMLUtils;

    @Autowired
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Autowired
    private ProdutoAtributoDataProvider produtoAtributoDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private RelatorioInventarioSintetico relatorioInventarioSintetico = new RelatorioInventarioSintetico();

    private List<RelatorioInventarioSintetico.ContaContabil> contasContabeis = new ArrayList<>();

    private RelatorioInventarioSintetico.ContaContabil conta = new RelatorioInventarioSintetico.ContaContabil();

    @Autowired
    private ConfigContaContabilDataProviderImpl configContaContabilDataProvider;

    public Arquivo executar(HalReportIntegrationProperties integrationProperties, List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado, UnidadeOrganizacional orgaoRelatorio, LocalDateTime dataFinal) {
        RelatorioInventarioSintetico relatorio = converterDadosRelatorio(registros, lancamentosContabeisAgrupado, orgaoRelatorio, dataFinal);
        Map<String, Object> dataSource = configurarDataSouce(integrationProperties, relatorio);
        ConfiguracaoRelatorio configuracaoRelatorio = configurarRelatorio(dataSource);

        return gerarRelatorio(configuracaoRelatorio);
    }

    private RelatorioInventarioSintetico converterDadosRelatorio(List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado, UnidadeOrganizacional orgaoRelatorio, LocalDateTime dataFinal) {
        inicializaRelatorio();

        List<ContaContabil> contasContabeisList = registros.stream().map(RelatorioSintetico::getContaContabil).distinct().collect(Collectors.toList());
        for (ContaContabil contaContabil : contasContabeisList) {
            List<RelatorioSintetico> registroPorContaContabil = registros.stream().filter(reg -> reg.getContaContabil().getCodigo().equals(contaContabil.getCodigo())).collect(Collectors.toList());
            setaTotalDeBens(registroPorContaContabil);
            setaValorAquisicao(registroPorContaContabil, lancamentosContabeisAgrupado);
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

    private void setaValorAquisicao(List<RelatorioSintetico> relatorio, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado) {
        List<RelatorioSintetico> relatoriosDistintos = relatorio.stream().filter(distinctByKey(RelatorioSintetico::getPatrimonioId)).collect(Collectors.toList());

        relatoriosDistintos.forEach(relatorioDistindo -> {
            lancamentosContabeisAgrupado.forEach(lca -> {
                if (relatorioDistindo.getPatrimonioId().equals(lca.getPatrimonio().getId())) {
                    conta.setValorAquisicao(conta.getValorAquisicao().add(lca.getMaiorValorLiquido(), new MathContext(10)).setScale(2, RoundingMode.HALF_EVEN));
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
            if (conta.getTipo().equals("AMORTIZAVEL")) {
                relatorioInventarioSintetico.setTemContaContabilAmortizavel(true);
            }
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
        return StringUtils.capitalize(mes) + "/" + ano;
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

    private Map<String, Object> configurarDataSouce(HalReportIntegrationProperties integrationProperties, RelatorioInventarioSintetico relatorio) {
        Map<String, Object> parametros = new HashMap<>();
        NumberFormat df = NumberFormat.getCurrencyInstance();
        parametros.put("properties", integrationProperties);
        parametros.put("relatorio", relatorio);
        parametros.put("dataHoraUsuario", getDataHoraEUsuarioNome());
        parametros.put("df", df);

        carregarLogo(parametros);

        return parametros;
    }

    private String getDataHoraEUsuarioNome() {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return data + " " + usuarioDataProvider.buscarUsuarioPorSessao(sessaoUsuarioDataProvider.get()).getNome();
    }

    private ConfiguracaoRelatorio configurarRelatorio(Map<String, Object> dataSource) {
        return reportHTMLUtils.construirConfiguracaoPadrao(NOME_RELATORIO, dataSource)
            .setNomeTemplateHeader("header-default")
            .setNomeTemplateFooter("footer-default");
    }

    private void carregarLogo(Map<String, Object> parametros) {
        String valor = produtoAtributoDataProvider.getValor(LOGO_ATRIBUTO);
        Arquivo arquivo = sistemaDeArquivosIntegration.download(valor);
        reportHTMLUtils.carregadorImage(parametros, arquivo.getContent(), LOGO_ATRIBUTO);
    }

    private Arquivo gerarRelatorio(ConfiguracaoRelatorio configuracaoRelatorio) {
        try {
            byte[] content = reportHTMLUtils.baixar(configuracaoRelatorio);

            return Arquivo
                .builder()
                .contentType("application/pdf")
                .nome(String.format("%s.pdf", NOME_RELATORIO))
                .content(content)
                .build();
        } catch (Exception e) {
            throw new RelatorioException("Não foi possivel realizar a geração do relatorio.", e);
        }
    }
}
