package br.com.azi.patrimoniointangivel.configuration.factory.gateway.integration;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.SistemaDeRelatoriosIntegrationHalReportImpl;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticopdf.GerarInventarioAnaliticoPDFUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticoxls.GerarInventarioAnaliticoXLSUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticopdf.GerarInventarioSinteticoPDFUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventariosinteticoxls.GerarInventarioSinteticoXLSUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarrelatoriolistagempatrimonioxls.GerarRelatorioListagemPatrimonioXLSUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarrelatoriomemorandopdf.GerarMemorandoPDFUseCase;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarrelatoriomovimentacaomemorandopdf.GerarMemorandoMovimentacaoPDFUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RefreshScope
@Configuration
@ConditionalOnProperty(prefix = "az.patrimonio-config.integration", name = "sistema-de-gestao-administrativa", havingValue = "hal-report", matchIfMissing = true)
public class SistemaDeRelatoriosIntegrationHalReportFactory {

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private GerarInventarioAnaliticoPDFUseCase gerarInventarioAnaliticoPDFUseCase;

    @Autowired
    private GerarInventarioAnaliticoXLSUseCase gerarInventarioAnaliticoXLSUseCase;

    @Autowired
    private GerarInventarioSinteticoPDFUseCase gerarInventarioSinteticoPDFUseCase;

    @Autowired
    private GerarInventarioSinteticoXLSUseCase gerarInventarioSinteticoXLSUseCase;

    @Autowired
    private GerarMemorandoPDFUseCase gerarMemorandoPDFUseCase;

    @Autowired
    private GerarMemorandoMovimentacaoPDFUseCase gerarMemorandoMovimentacaoPDFUseCase;

    @Autowired
    private GerarRelatorioListagemPatrimonioXLSUseCase gerarRelatorioListagemPatrimonioXLSUseCase;

    @Bean("SistemaDeRelatoriosIntegration")
    @Primary
    public SistemaDeRelatoriosIntegration createBean() {
        HalReportIntegrationProperties halProperties = getProperties();

        return new SistemaDeRelatoriosIntegrationHalReportImpl(
            halProperties,
            gerarInventarioAnaliticoPDFUseCase,
            gerarInventarioAnaliticoXLSUseCase,
            gerarInventarioSinteticoPDFUseCase,
            gerarInventarioSinteticoXLSUseCase,
            gerarRelatorioListagemPatrimonioXLSUseCase,
            gerarMemorandoPDFUseCase,
            gerarMemorandoMovimentacaoPDFUseCase
        );
    }

    public HalReportIntegrationProperties getProperties() {
        return HalReportIntegrationProperties
            .builder()
            .header(HalReportIntegrationProperties.Header
                .builder()
                .titulo1(patrimonioIntangivelProperties.getRelatorio().getHeader().getTitulo1())
                .titulo2(patrimonioIntangivelProperties.getRelatorio().getHeader().getTitulo2())
                .build())
            .footer(HalReportIntegrationProperties.Footer
                .builder()
                .rodape1(patrimonioIntangivelProperties.getRelatorio().getFooter().getRodape1())
                .rodape2(patrimonioIntangivelProperties.getRelatorio().getFooter().getRodape2())
                .build())
            .build();
    }
}
