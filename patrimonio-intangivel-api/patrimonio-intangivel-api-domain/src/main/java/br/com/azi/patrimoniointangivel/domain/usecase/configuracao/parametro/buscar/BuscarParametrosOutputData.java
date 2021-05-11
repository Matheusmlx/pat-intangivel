package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarParametrosOutputData {
    private Short vidaUtilSemLicenca;
    private String codigoChat;
    private String dataLimiteRetroativo;
    private String mensagemRodapeMemorandoMovimentacao;
    private String dataCorteInicioCadastroRetroativo;
    private Boolean mostrarBotaoVidaUtilIndefinida;
}
