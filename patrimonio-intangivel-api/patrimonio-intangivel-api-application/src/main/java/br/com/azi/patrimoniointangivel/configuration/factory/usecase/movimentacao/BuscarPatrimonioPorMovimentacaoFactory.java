package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoMovimentacaoUseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.BuscarPatrimonioPorMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarPatrimonioPorMovimentacao.converter.BuscarPatrimonioPorMovimentacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarPatrimonioPorMovimentacaoFactory {

    @Autowired
    PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarPatrimonioPorMovimentacaoUseCase")
    @DependsOn({"BuscarPatrimonioPorMovimentacaoOutputDataConverter"})
    public BuscarPatrimonioPorMovimentacaoUseCase createUseCase(BuscarPatrimonioPorMovimentacaoOutputDataConverter outputDataConverter) {
        return new BuscarPatrimonioPorMovimentacaoMovimentacaoUseImpl(patrimonioDataProvider, outputDataConverter);
    }

    @Bean("BuscarPatrimonioPorMovimentacaoOutputDataConverter")
    public BuscarPatrimonioPorMovimentacaoOutputDataConverter createConverter() {
        return new BuscarPatrimonioPorMovimentacaoOutputDataConverter();
    }
}
