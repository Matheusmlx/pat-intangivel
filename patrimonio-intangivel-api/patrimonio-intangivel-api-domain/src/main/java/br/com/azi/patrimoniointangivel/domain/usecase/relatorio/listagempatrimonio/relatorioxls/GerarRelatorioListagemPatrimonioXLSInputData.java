package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.listagempatrimonio.relatorioxls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GerarRelatorioListagemPatrimonioXLSInputData {
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
    private List<Long> unidadeOrganizacionalIds;
    private String reconhecimento;
    private BigDecimal valorAquisicaoInicial;
    private BigDecimal valorAquisicaoFinal;
    private BigDecimal valorLiquidoInicial;
    private BigDecimal valorLiquidoFinal;
    private String vencimentoLicencaInicio;
    private String vencimentoLicencaFim;
    private String dataDeNlInicio;
    private String dataDeNlFim;
    private String dataAtivacaoInicio;
    private String dataAtivacaoFim;
    private String dataAquisicaoInicio;
    private String dataAquisicaoFim;
}
