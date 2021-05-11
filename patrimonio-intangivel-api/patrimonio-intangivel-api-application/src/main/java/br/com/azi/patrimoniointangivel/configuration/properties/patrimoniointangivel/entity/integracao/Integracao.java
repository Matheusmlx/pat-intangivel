package br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.integracao;

import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.integracao.hal.Hal;
import br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.integracao.hal.HalConfig;
import lombok.Data;

@Data
public class Integracao {
    String sistemaDeArquivos;
    String sistemaDeGestaoAdministrativa;
    String sistemaDeFornecedores;

    Efornecedor efornecedor = new Efornecedor();
    Hal hal = new Hal();
    HalConfig halConfig = new HalConfig();
    Usuario usuario = new Usuario();
}
