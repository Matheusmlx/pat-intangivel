package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropriedadesProjeto {
    private String cronDiaAgendamentoAmortizacao;
    private String dataMensalAmortizacao;
    private CodigoContaContabil codigoContaContabil;
    private List<String> feriadosNacionais;
    private List<String> feriadosLocais;
    private String quantidadeDigitosNumeroPatrimonio;
    private Short vidaUtilSemLicenca;
    private String codigoChat;
    private String dataLimiteRetroativo;
    private String mensagemRodapeMemorandoMovimentacao;
    private String dataCorteInicioCadastroRetroativo;
    private Boolean mostrarBotaoVidaUtilIndefinida;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CodigoContaContabil {
        String softwareDesenvolvimento;
        String bensIntangiveisSoftware;
        String marcasPatentesIndustriais;
        String concessaoDireitosUsoComunicacao;
        String direitosAutorais;
        String direitosRecursosNaturais;
        String adiantamentoTransferenciaTecnologia;
        String outrosDireitosBensIntangiveis;
    }
}
