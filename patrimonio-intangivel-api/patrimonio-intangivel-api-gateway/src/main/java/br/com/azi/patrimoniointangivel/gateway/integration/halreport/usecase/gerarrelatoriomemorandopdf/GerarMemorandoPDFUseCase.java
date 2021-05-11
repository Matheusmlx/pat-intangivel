package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarrelatoriomemorandopdf;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorando;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ProdutoAtributoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.ConfiguracaoRelatorio;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.utils.ReportHTMLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Component
public class GerarMemorandoPDFUseCase {

    private static final String NOME_RELATORIO = "relatoriomemorando";

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

    public Arquivo executar(HalReportIntegrationProperties integrationProperties, RelatorioMemorando relatorio) {
        Map<String, Object> dataSource = configurarDataSouce(integrationProperties, relatorio);
        ConfiguracaoRelatorio configuracaoRelatorio = configurarRelatorio(dataSource);

        return gerarRelatorio(configuracaoRelatorio);
    }

    private Map<String, Object> configurarDataSouce(HalReportIntegrationProperties integrationProperties, RelatorioMemorando relatorio){
        NumberFormat df = NumberFormat.getCurrencyInstance();
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("properties", integrationProperties);
        parametros.put("relatorio", relatorio);
        parametros.put("dataHoraUsuario", getDataHoraEUsuarioNome());
        parametros.put("df",df);

        carregarLogo(parametros, LOGO_ATRIBUTO);

        return parametros;
    }

    private String getDataHoraEUsuarioNome() {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return data + " " + usuarioDataProvider.buscarUsuarioPorSessao(sessaoUsuarioDataProvider.get()).getNome();
    }

    private void carregarLogo(Map<String, Object> parametros, String logo) {
        String valor = produtoAtributoDataProvider.getValor(logo);
        Arquivo arquivo = sistemaDeArquivosIntegration.download(valor);
        reportHTMLUtils.carregadorImage(parametros, arquivo.getContent(), logo);
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
