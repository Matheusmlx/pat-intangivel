package br.com.azi.patrimoniointangivel.configuration.properties.patrimoniointangivel.entity.relatorio;

import lombok.Data;

@Data
public class Relatorio {
    Header header = new Header();
    Footer footer = new Footer();
}
