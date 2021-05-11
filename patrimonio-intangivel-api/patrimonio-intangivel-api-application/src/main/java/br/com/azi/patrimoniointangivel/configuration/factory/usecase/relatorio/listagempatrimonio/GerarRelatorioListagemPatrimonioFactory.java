package br.com.azi.patrimoniointangivel.configuration.factory.usecase.relatorio.listagempatrimonio;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.GerarRelatorioListagemPatrimonioXLSUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.converter.GerarRelatorioListagemPatrimonioConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls.converter.GerarRelatorioListagemPatrimonioXLSOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

@Configuration
public class GerarRelatorioListagemPatrimonioFactory {

    @Autowired
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private GerarRelatorioListagemPatrimonioConverter gerarRelatorioListagemPatrimonioConverter;

    @Autowired
    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;


    @Bean("GerarRelatorioListagemPatrimonioXLSUseCase")
    @DependsOn()
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GerarRelatorioListagemPatrimonioXLSUseCaseImpl createUseCase(GerarRelatorioListagemPatrimonioConverter filtroConverter,GerarRelatorioListagemPatrimonioXLSOutputDataConverter outputDataConverter){
        return new GerarRelatorioListagemPatrimonioXLSUseCaseImpl(
            patrimonioDataProvider,
            sistemaDeRelatoriosIntegration,
            filtroConverter,
            sistemaDeGestaoAdministrativaIntegration,
            outputDataConverter
        );
    }

    @Bean("GerarRelatorioListagemPatrimonioConverter")
    public GerarRelatorioListagemPatrimonioConverter createFiltroConverter(){return new GerarRelatorioListagemPatrimonioConverter();}

    @Bean("GerarRelatorioListagemPatrimonioXLSOutputDataConverter")
    public GerarRelatorioListagemPatrimonioXLSOutputDataConverter createOutputDataConverter(){
        return new GerarRelatorioListagemPatrimonioXLSOutputDataConverter();
    }


}


