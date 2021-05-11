package br.com.azi.patrimoniointangivel.configuration.factory.usecase.patrimonio;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.entity.CodigoContaContabil;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.EditarPatrimonioUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.EditarPatrimonioUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.converter.EditarPatrimonioOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class EditarPatrimonioFactory {

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Autowired
    private ContaContabilDataProvider contaContabilDataProvider;

    @Autowired
    private PatrimonioIntangivelProperties patrimonioIntangivelProperties;

    @Autowired
    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Bean("EditarPatrimonioUseCase")
    @DependsOn({"EditarPatrimonioOutputDataConverter"})
    public EditarPatrimonioUseCase createUseCase(EditarPatrimonioOutputDataConverter outputDataConverter) {
        return new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            outputDataConverter,
            contaContabilDataProvider,
            CodigoContaContabil
                .builder()
                .bensIntangiveisSoftware(patrimonioIntangivelProperties.getCodigoContaContabil().getBensIntangiveisSoftware())
                .softwareDesenvolvimento(patrimonioIntangivelProperties.getCodigoContaContabil().getSoftwareDesenvolvimento())
                .adiantamentoTransferenciaTecnologia(patrimonioIntangivelProperties.getCodigoContaContabil().getAdiantamentoTransferenciaTecnologia())
                .concessaoDireitosUsoComunicacao(patrimonioIntangivelProperties.getCodigoContaContabil().getConcessaoDireitosUsoComunicacao())
                .direitosAutorais(patrimonioIntangivelProperties.getCodigoContaContabil().getDireitosAutorais())
                .direitosRecursosNaturais(patrimonioIntangivelProperties.getCodigoContaContabil().getDireitosRecursosNaturais())
                .marcasPatentesIndustriais(patrimonioIntangivelProperties.getCodigoContaContabil().getMarcasPatentesIndustriais())
                .outrosDireitosBensIntangiveis(patrimonioIntangivelProperties.getCodigoContaContabil().getOutrosDireitosBensIntangiveis())
                .build(),
            notaLancamentoContabilDataProvider
        );
    }

    @Bean("EditarPatrimonioOutputDataConverter")
    public EditarPatrimonioOutputDataConverter createConverter() {
        return new EditarPatrimonioOutputDataConverter();
    }
}
