package br.com.azi.patrimoniointangivel.configuration.factory.usecase.movimentacao;

import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.CriarMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.converter.CriarMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CriarMovimentacaoFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private GerarCodigoDeMovimentacaoUseCase gerarCodigoDeMovimentacaoUseCase;

   @Bean("CriarMovimentacaoUseCase")
   @DependsOn({"CriarMovimentacaoOutputDataConverter"})
    public CriarMovimentacaoUseCase createUseCase(CriarMovimentacaoOutputDataConverter outputDataConverter){
       return  new CriarMovimentacaoUseCaseImpl(
           patrimonioDataProvider,
           movimentacaoDataProvider,
           outputDataConverter,
           gerarCodigoDeMovimentacaoUseCase);
   }

   @Bean("CriarMovimentacaoOutputDataConverter")
    public CriarMovimentacaoOutputDataConverter createConverter(){
       return new CriarMovimentacaoOutputDataConverter();
   }

}
