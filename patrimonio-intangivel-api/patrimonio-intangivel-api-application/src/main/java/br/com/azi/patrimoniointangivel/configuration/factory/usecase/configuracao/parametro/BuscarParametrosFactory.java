package br.com.azi.patrimoniointangivel.configuration.factory.usecase.configuracao.parametro;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.PatrimonioIntangivelProperties;
import br.com.azi.patrimoniointangivel.domain.entity.PropriedadesProjeto;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosUseCaseImpl;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.converter.BuscarParametrosOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarParametrosFactory {

    @Autowired
    private PatrimonioIntangivelProperties properties;

    @Bean("BuscarParametrosUseCase")
    @DependsOn({"BuscarParametrosOutputDataConverter"})
    public BuscarParametrosUseCase createUseCase(BuscarParametrosOutputDataConverter outputDataConverter) {
        return new BuscarParametrosUseCaseImpl(
            PropriedadesProjeto
                .builder()
                .cronDiaAgendamentoAmortizacao(properties.getCronDiaAgendamentoAmortizacao())
                .mensagemRodapeMemorandoMovimentacao(properties.getMensagemRodapeMemorandoMovimentacao())
                .dataMensalAmortizacao(properties.getDataMensalAmortizacao())
                .feriadosNacionais(properties.getFeriadosNacionais())
                .feriadosLocais(properties.getFeriadosLocais())
                .quantidadeDigitosNumeroPatrimonio(properties.getQuantidadeDigitosNumeroPatrimonio())
                .vidaUtilSemLicenca(properties.getVidaUtilSemLicenca())
                .codigoChat(properties.getCodigoChat())

                .dataLimiteRetroativo(properties.getDataLimiteAtivacaoRetroativa())
                .codigoContaContabil(
                    PropriedadesProjeto.CodigoContaContabil
                        .builder()
                        .softwareDesenvolvimento(properties.getCodigoContaContabil().getSoftwareDesenvolvimento())
                        .bensIntangiveisSoftware(properties.getCodigoContaContabil().getBensIntangiveisSoftware())
                        .marcasPatentesIndustriais(properties.getCodigoContaContabil().getMarcasPatentesIndustriais())
                        .concessaoDireitosUsoComunicacao(properties.getCodigoContaContabil().getConcessaoDireitosUsoComunicacao())
                        .direitosAutorais(properties.getCodigoContaContabil().getDireitosAutorais())
                        .direitosRecursosNaturais(properties.getCodigoContaContabil().getDireitosRecursosNaturais())
                        .adiantamentoTransferenciaTecnologia(properties.getCodigoContaContabil().getAdiantamentoTransferenciaTecnologia())
                        .outrosDireitosBensIntangiveis(properties.getCodigoContaContabil().getOutrosDireitosBensIntangiveis())
                        .build()
                )
                .dataCorteInicioCadastroRetroativo(properties.getDataCorteInicioCadastroRetroativo())
                .mostrarBotaoVidaUtilIndefinida(properties.getMostrarBotaoVidaUtilIndefinida())
                .build(),
            outputDataConverter);
    }

    @Bean("BuscarParametrosOutputDataConverter")
    public BuscarParametrosOutputDataConverter createConverter() {
        return new BuscarParametrosOutputDataConverter();
    }
}
