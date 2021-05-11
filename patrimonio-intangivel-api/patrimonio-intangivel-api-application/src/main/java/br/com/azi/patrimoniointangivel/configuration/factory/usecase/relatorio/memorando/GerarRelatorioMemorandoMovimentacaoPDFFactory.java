package br.com.azi.patrimoniointangivel.configuration.factory.usecase.relatorio.memorando;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.GerarRelatorioMemorandoMovimentacaoPDFUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.converter.GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class GerarRelatorioMemorandoMovimentacaoPDFFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Autowired
    private AmortizacaoDataProvider amortizacaoDataProvider;

    @Autowired
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Autowired
    private GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase;

    @Autowired
    private GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter outputDataConverter;

    @Autowired
    private BuscarParametrosUseCase buscarParametrosUseCase;

    @Bean("GerarRelatorioMemorandoMovimentacaoPDFUseCase")
    @DependsOn("GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GerarRelatorioMemorandoMovimentacaoPDFUseCaseImpl createUseCase(){
        return new GerarRelatorioMemorandoMovimentacaoPDFUseCaseImpl(movimentacaoDataProvider,
            lancamentosContabeisDataProvider,
            amortizacaoDataProvider,
            gerarNumeroMemorandoUseCase,
            sistemaDeRelatoriosIntegration,
            outputDataConverter,
            buscarParametrosUseCase);
    }

    @Bean("GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter")
    public GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter createOutputDataConverter(){
        return new GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter();
    }
}
