package br.com.azi.patrimoniointangivel.configuration.factory.usecase.relatorio.memorando;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.GerarRelatorioMemorandoPDFUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatorio.converter.GerarRelatorioMemorandoPDFOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.historicomemorando.GerarHistoricoMemorandoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class GerarRelatorioMemorandoPDFFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Autowired
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Autowired
    private GerarRelatorioMemorandoPDFOutputDataConverter outputDataConverter;

    @Autowired
    private GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase;

    @Autowired
    private GerarHistoricoMemorandoUseCase gerarHistoricoMemorandoUseCase;

    @Bean("GerarRelatorioMemorandoPDFUseCase")
    @DependsOn("GerarRelatorioMemorandoPDFOutputDataConverter")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GerarRelatorioMemorandoPDFUseCaseImpl createUseCase(){
        return new GerarRelatorioMemorandoPDFUseCaseImpl(patrimonioDataProvider,
            sistemaDeRelatoriosIntegration,
            outputDataConverter,
            gerarNumeroMemorandoUseCase,
            gerarHistoricoMemorandoUseCase,
            lancamentosContabeisDataProvider);
    }

    @Bean("GerarRelatorioMemorandoPDFOutputDataConverter")
    public GerarRelatorioMemorandoPDFOutputDataConverter createOutputDataConverter(){
        return new GerarRelatorioMemorandoPDFOutputDataConverter();
    }
}
