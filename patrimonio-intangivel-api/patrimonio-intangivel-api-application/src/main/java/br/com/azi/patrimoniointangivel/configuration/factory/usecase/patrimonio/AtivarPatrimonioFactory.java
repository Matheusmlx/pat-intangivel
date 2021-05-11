package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.AtivarPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.converter.AtivarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class AtivarPatrimonioFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase;

    @Autowired
    private SalvaConfigAmortizacaoUseCase salvaConfigAmortizacaoUseCase;

    @Autowired
    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    @Autowired
    private ContaContabilDataProvider contaContabilDataProvider;

    @Autowired
    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    @Autowired
    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Bean("AtivarPatrimonioUseCase")
    @DependsOn({"AtivarPatrimonioOutputDataConverter"})
    public AtivarPatrimonioUseCaseImpl createUseCase(AtivarPatrimonioOutputDataConverter outputDataConverter) {
        return new AtivarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            outputDataConverter,
            patrimonioIntangivelProperties.getVidaUtilSemLicenca(),
            gerarNumeroPatrimonioUseCase,
            salvaConfigAmortizacaoUseCase,
            dadosAmortizacaoDataProvider,
            contaContabilDataProvider,
            configContaContabilDataProvider,
            lancamentosContabeisDataProvider,
            patrimonioIntangivelProperties.getDataCorteInicioCadastroRetroativo());
    }

    @Bean("AtivarPatrimonioOutputDataConverter")
    public AtivarPatrimonioOutputDataConverter createConverter() {
        return new AtivarPatrimonioOutputDataConverter();
    }
}
