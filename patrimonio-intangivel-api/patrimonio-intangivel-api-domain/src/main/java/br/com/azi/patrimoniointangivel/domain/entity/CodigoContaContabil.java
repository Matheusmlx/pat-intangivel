package br.com.azi.patrimoniointangivel.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodigoContaContabil {

    String softwareDesenvolvimento;
    String bensIntangiveisSoftware;
    String marcasPatentesIndustriais;
    String concessaoDireitosUsoComunicacao;
    String direitosAutorais;
    String direitosRecursosNaturais;
    String adiantamentoTransferenciaTecnologia;
    String outrosDireitosBensIntangiveis;

}
