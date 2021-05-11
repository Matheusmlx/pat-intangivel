package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.entity.CodigoContaContabil;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.CadastraPatrimonioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.CadastraPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.converter.CadastraPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CadastraPatrimonioFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private ContaContabilDataProvider contaContabilDataProvider;

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase;

    @Bean("CadastraPatrimonioUseCase")
    @DependsOn({"CadastraPatrimonioOutputDataConverter"})
    public CadastraPatrimonioUseCase createUseCase(CadastraPatrimonioOutputDataConverter outputDataConverter) {
        return new CadastraPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            outputDataConverter,
            contaContabilDataProvider,
            gerarNumeroMemorandoUseCase,
            CodigoContaContabil
                .builder()
                .adiantamentoTransferenciaTecnologia(patrimonioIntangivelProperties.getCodigoContaContabil().getAdiantamentoTransferenciaTecnologia())
                .concessaoDireitosUsoComunicacao(patrimonioIntangivelProperties.getCodigoContaContabil().getConcessaoDireitosUsoComunicacao())
                .direitosAutorais(patrimonioIntangivelProperties.getCodigoContaContabil().getDireitosAutorais())
                .direitosRecursosNaturais(patrimonioIntangivelProperties.getCodigoContaContabil().getDireitosRecursosNaturais())
                .marcasPatentesIndustriais(patrimonioIntangivelProperties.getCodigoContaContabil().getMarcasPatentesIndustriais())
                .outrosDireitosBensIntangiveis(patrimonioIntangivelProperties.getCodigoContaContabil().getOutrosDireitosBensIntangiveis())
                .build());
    }

    @Bean("CadastraPatrimonioOutputDataConverter")
    public CadastraPatrimonioOutputDataConverter createConverter() {
        return new CadastraPatrimonioOutputDataConverter();
    }


}
