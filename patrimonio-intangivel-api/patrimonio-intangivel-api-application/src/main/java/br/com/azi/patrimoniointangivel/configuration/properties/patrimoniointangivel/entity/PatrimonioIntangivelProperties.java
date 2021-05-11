package br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.codigocontacontabil.CodigoContaContabil;
import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.integracao.Integracao;
import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.relatorio.Relatorio;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "az.patrimonio-intangivel")
public class PatrimonioIntangivelProperties {
    CodigoContaContabil codigoContaContabil = new CodigoContaContabil();
    Map<String, Object> rotulosPersonalizados;
    Integracao integracao = new Integracao();
    Relatorio relatorio = new Relatorio();
    List<String> feriadosNacionais;
    List<String> feriadosLocais;
    String quantidadeDigitosNumeroPatrimonio;
    String quantidadeDigitosCodigoMovimentacao;
    Short vidaUtilSemLicenca;
    String codigoChat;
    String cronDiaAgendamentoAmortizacao;
    String dataMensalAmortizacao;
    String dataLimiteAtivacaoRetroativa;
    String mensagemRodapeMemorandoMovimentacao;
    String dataCorteInicioCadastroRetroativo;
    Boolean mostrarBotaoVidaUtilIndefinida;
}
