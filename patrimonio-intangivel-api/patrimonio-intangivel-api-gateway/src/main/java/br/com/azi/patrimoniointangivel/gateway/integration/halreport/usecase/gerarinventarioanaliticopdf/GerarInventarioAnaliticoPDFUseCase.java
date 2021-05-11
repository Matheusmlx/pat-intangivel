package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticopdf;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioAnalitico;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ProdutoAtributoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.ConfiguracaoRelatorio;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticopdf.entity.RelatorioInventarioAnalitico;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.utils.ReportHTMLUtils;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class GerarInventarioAnaliticoPDFUseCase {

    private static final String NOME_RELATORIO = "inventarioanalitico";

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

    public Arquivo executar(HalReportIntegrationProperties integrationProperties, RelatorioAnalitico registros) {
        RelatorioInventarioAnalitico relatorio = converterDadosRelatorio(registros);
        Map<String, Object> dataSource = configurarDataSouce(integrationProperties, relatorio);
        ConfiguracaoRelatorio configuracaoRelatorio = configurarRelatorio(dataSource);

        return gerarRelatorio(configuracaoRelatorio);
    }

    private RelatorioInventarioAnalitico converterDadosRelatorio(RelatorioAnalitico registros) {
        RelatorioInventarioAnalitico relatorioInventarioAnalitico = new RelatorioInventarioAnalitico();
        relatorioInventarioAnalitico.setNomeOrgao(retornaNomeOrgao(registros.getOrgao()));
        relatorioInventarioAnalitico.setTotalValorLiquido(registros.getOrgao().getTotalValorLiquido());
        relatorioInventarioAnalitico.setTotalAmortizacaoAcumulada(registros.getOrgao().getTotalAmortizacaoAcumulada());
        relatorioInventarioAnalitico.setTotalAmortizacaoMensal(registros.getOrgao().getTotalAmortizacaoMensal());
        relatorioInventarioAnalitico.setTotalDeBens(retornaTotalBensFormatado(registros.getOrgao()));
        relatorioInventarioAnalitico.setDataRelatorio(criarDataRelatorio(registros.getDataRelatorio()));
        relatorioInventarioAnalitico.setTotalValorAquisicao(registros.getOrgao().getTotalValorAquisicao());
        relatorioInventarioAnalitico.setContasContabeis(registros.getContasContabeis().stream().map(this::converterItem).collect(Collectors.toList()));

        relatorioInventarioAnalitico.getContasContabeis().sort(Comparator.comparing(RelatorioInventarioAnalitico.ContaContabil::getCodigo));

        return relatorioInventarioAnalitico;

    }

    private static final ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new ItemOutputDataConverter();

    private RelatorioInventarioAnalitico.ContaContabil converterItem(RelatorioAnalitico.ContaContabilIn source) {
        return ITEM_OUTPUT_DATA_CONVERTER.to(source);
    }

    private static class ItemOutputDataConverter extends GenericConverter<RelatorioAnalitico.ContaContabilIn, RelatorioInventarioAnalitico.ContaContabil> {
    }

    private String retornaNomeOrgao(RelatorioAnalitico.Orgao orgao){
        return orgao.getNome() + " - " + orgao.getSigla();
    }

    private String retornaTotalBensFormatado(RelatorioAnalitico.Orgao orgao) {
        if(orgao.getTotalDeBens() < 2) {
            return orgao.getTotalDeBens()+" Patrimônio";
        }
        return orgao.getTotalDeBens()+" Patrimônios";
    }

    private Map<String, Object> configurarDataSouce(HalReportIntegrationProperties integrationProperties, RelatorioInventarioAnalitico relatorio){
        NumberFormat df = NumberFormat.getCurrencyInstance();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("properties", integrationProperties);
        parametros.put("relatorio", relatorio);
        parametros.put("dataHoraUsuario", getDataHoraEUsuarioNome());
        parametros.put("df",df);

        carregarLogo(parametros);

        return parametros;
    }

    private String getDataHoraEUsuarioNome() {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return data + " " + usuarioDataProvider.buscarUsuarioPorSessao(sessaoUsuarioDataProvider.get()).getNome();
    }

    private String criarDataRelatorio(LocalDateTime dataFinal){
        String mes = dataFinal.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt"));
        String ano = String.valueOf(dataFinal.getYear());
        return StringUtils.capitalize(mes)+ "/" + ano;
    }

    private void carregarLogo(Map<String, Object> parametros) {
        String valor = produtoAtributoDataProvider.getValor(LOGO_ATRIBUTO);
        Arquivo arquivo = sistemaDeArquivosIntegration.download(valor);
        reportHTMLUtils.carregadorImage(parametros, arquivo.getContent(), LOGO_ATRIBUTO);
    }


    private ConfiguracaoRelatorio configurarRelatorio(Map<String, Object> dataSource) {
        return reportHTMLUtils.construirConfiguracaoPadrao(NOME_RELATORIO, dataSource)
            .setNomeTemplateHeader("header-default")
            .setNomeTemplateFooter("footer-default");
    }

    private Arquivo gerarRelatorio(ConfiguracaoRelatorio configuracaoRelatorio){
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
