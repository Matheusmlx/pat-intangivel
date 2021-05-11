package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticoxls;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioAnalitico;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarinventarioanaliticopdf.entity.RelatorioInventarioAnalitico;
import br.com.azi.patrimoniointangivel.gateway.integration.utils.XLSFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class GerarInventarioAnaliticoXLSUseCase {

    private static final String NOME_RELATORIO = "inventarioAnalitico";

    private static final String[] COLUNAS = {"N° Patrimônio", "Patrimônio", "Tipo", "Amortizável", "Valor de Entrada (R$)", "Amort.Mensal (R$)","Amort.Acumulada (R$)","Valor Líquido (R$)"};

    private static final int ALTURA_DA_LINHA = 40;

    public Arquivo executar(HalReportIntegrationProperties integrationProperties, RelatorioAnalitico registros) {

        RelatorioInventarioAnalitico relatorio = converterDadosRelatorio(registros);
        List<Map<Integer, Object>> dataSource = configurarDataSouce(integrationProperties, registros);
        XLSFactory xlsFactory = configurarRelatorio(relatorio);

        return gerarRelatorio(xlsFactory, dataSource);
    }

    private RelatorioInventarioAnalitico converterDadosRelatorio(RelatorioAnalitico registros) {
        RelatorioInventarioAnalitico relatorioInventarioAnalitico = new RelatorioInventarioAnalitico();
        relatorioInventarioAnalitico.setNomeOrgao(retornaNomeOrgao(registros.getOrgao()));
        return relatorioInventarioAnalitico;
    }

    private String retornaNomeOrgao(RelatorioAnalitico.Orgao orgao){
        return orgao.getNome() + " - " + orgao.getSigla();
    }

    private Map<Integer, Object> getMapItem(RelatorioInventarioAnalitico.Patrimonio patrimonio) {
        Map<Integer, Object> item = new HashMap<>();

        item.put(0, patrimonio.getNumero());
        item.put(1, patrimonio.getNome());
        item.put(2, patrimonio.getTipo());
        item.put(3, patrimonio.getAmortizavel() ? "Sim" : "Não");
        item.put(4, patrimonio.getValorAquisicao().doubleValue());
        item.put(5, patrimonio.getAmortizavel() ? patrimonio.getValorAmortizadoMensal().doubleValue() : "-");
        item.put(6, patrimonio.getAmortizavel() ? patrimonio.getValorAmortizadoAcumulado().doubleValue() : "-" );
        item.put(7, patrimonio.getValorLiquido().doubleValue());

        return item;
    }

    private XLSFactory configurarRelatorio(RelatorioInventarioAnalitico relatorio) {
        return XLSFactory.getInstance(COLUNAS.length, "dd/MM/yyyy")
            .inserirTituloLinha(relatorio.getNomeOrgao())
            .inserirCabecalhos(COLUNAS);
    }

    private Arquivo gerarRelatorio(XLSFactory xlsFactory, List<Map<Integer, Object>> dados) {

        ArrayList<String> typesCells =setTypesCells();

        try {
            xlsFactory = xlsFactory.inserirDadosNasCelulas(dados,typesCells)
                .autoSizeColumns(COLUNAS.length)
                .setSizeColumn(0,5000)
                .setSizeColumn(1,5000)
                .setSizeColumn(2,5000)
                .setSizeColumn(3,5000)
                .setSizeColumn(4,8000)
                .setSizeColumn(5,7000)
                .setSizeColumn(6,7000)
                .setSizeColumn(7,5000)
                .autoSizeRows(dados.size(), ALTURA_DA_LINHA);

            byte[] content = xlsFactory.build();

            return Arquivo
                .builder()
                .contentType("application/x-msexcel")
                .nome(String.format("%s.pdf", NOME_RELATORIO))
                .content(content)
                .build();
        } catch (Exception e) {
            throw new RelatorioException("Não foi possivel realizar a geração do relatorio.", e);
        }
    }

    private ArrayList<String> setTypesCells(){
        ArrayList<String> cellsTypes = new ArrayList<>();
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("R$#,##0.00");
        cellsTypes.add("R$#,##0.00");
        cellsTypes.add("R$#,##0.00");
        cellsTypes.add("R$#,##0.00");
        return cellsTypes;
    }

    private List<Map<Integer, Object>> configurarDataSouce(HalReportIntegrationProperties integrationProperties, RelatorioAnalitico relatorio){
        List<Map<Integer, Object>> dados = new ArrayList<>();

        for(RelatorioAnalitico.ContaContabilIn contaContabil: relatorio.getContasContabeis()){
            for (RelatorioAnalitico.Patrimonio patrimonio: contaContabil.getPatrimonios()) {
                dados.add(getMapItem(RelatorioInventarioAnalitico.Patrimonio
                    .builder()
                    .id(patrimonio.getId())
                    .valorLiquido(patrimonio.getValorLiquido())
                    .valorAquisicao(patrimonio.getValorAquisicao())
                    .valorAmortizadoMensal(patrimonio.getValorAmortizadoMensal())
                    .valorAmortizadoAcumulado(patrimonio.getValorAmortizadoAcumulado())
                    .tipo(patrimonio.getTipo())
                    .numero(patrimonio.getNumero())
                    .nome(patrimonio.getNome())
                    .amortizavel(patrimonio.getAmortizavel())
                    .build()));
            }
        }

        return dados;
    }



}
