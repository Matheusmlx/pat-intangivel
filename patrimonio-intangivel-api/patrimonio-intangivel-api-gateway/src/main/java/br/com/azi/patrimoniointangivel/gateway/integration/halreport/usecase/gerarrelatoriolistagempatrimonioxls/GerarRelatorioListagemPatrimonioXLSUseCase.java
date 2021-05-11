package br.com.azi.patrimoniointangivel.gateway.integration.halreport.usecase.gerarrelatoriolistagempatrimonioxls;

import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioListagemPatrimonios;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.entity.HalReportIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.halreport.exception.RelatorioException;
import br.com.azi.patrimoniointangivel.gateway.integration.utils.XLSFactory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class GerarRelatorioListagemPatrimonioXLSUseCase {

    private static final String NOME_RELATORIO = "inventarioListagemPatrimonio";

    private static final String[] COLUNAS = {"CÓDIGO/N° PATRIMÔNIO", "TIPO", "NOME", "ÓRGÃO", "SETOR", "DESCRIÇÃO", "SITUAÇÃO INTANGÍVEL", "VALOR DE AQUISIÇÃO (R$)", "VALOR DE ENTRADA (R$)", "RECONHECIMENTO", "DATA DE AQUISIÇÃO", "DATA DE ATIVAÇÃO", "DATA DA NL","NÚMERO DA NL", "FORNECEDOR", "VENCIMENTO DA LICENÇA", "CONTA CONTÁBIL", "SITUAÇÃO", "TAXA DE AMORTIZAÇÃO", "VALOR LÍQUIDO (R$)"};

    private static final int BASE_PORCENTAGEM = 100;

    private static final int ALTURA_DA_LINHA = 30;

    private static final String MASCARA_DATA = "dd/MM/yyyy";


    public Arquivo executar(HalReportIntegrationProperties integrationProperties, ListaPaginada<Patrimonio> registros) {

        List<Map<Integer, Object>> dataSource = converterDadosRelatorio(registros);
        XLSFactory xlsFactory = configurarRelatorio();
        return gerarRelatorio(xlsFactory, dataSource);
    }

    private List<Map<Integer, Object>> converterDadosRelatorio(ListaPaginada<Patrimonio> registros) {
        List<Map<Integer, Object>> dados = new ArrayList<>();

        registros.getItems()
            .forEach(patrimonio -> dados.add(getMapItem(RelatorioListagemPatrimonios.Patrimonio
                .builder()
                .id(patrimonio.getId())
                .numero(patrimonio.getNumero())
                .nome(Objects.isNull(patrimonio.getNome()) ? " " : patrimonio.getNome())
                .tipo(patrimonio.getTipo().getValor())
                .orgao(Objects.isNull(patrimonio.getOrgao()) ? " " : formatarOrgao(patrimonio.getOrgao()))
                .setor(Objects.isNull(patrimonio.getSetor()) ? " " : formatarOrgao(patrimonio.getSetor()))
                .situacaoPatrimonio(Objects.isNull(patrimonio.getSituacao()) ? " " : patrimonio.getSituacao().getValor())
                .estado(Objects.isNull(patrimonio.getEstado()) ? " " : patrimonio.getEstado().getValor())
                .descricao(Objects.isNull(patrimonio.getDescricao()) ? " " : patrimonio.getDescricao())
                .reconhecimento(Objects.isNull(patrimonio.getReconhecimento()) ? " " : patrimonio.getReconhecimento().getValor())
                .valorAquisicao(patrimonio.getValorAquisicao())
                .valorEntrada(patrimonio.getValorEntrada())
                .dataAquisicao(patrimonio.getDataAquisicao())
                .dataAtivacao(patrimonio.getDataAtivacao())
                .notaLancamentoContabil(Objects.isNull(patrimonio.getNotaLancamentoContabil()) ? NotaLancamentoContabil.builder().build() : patrimonio.getNotaLancamentoContabil())
                .vencimentoLicenca(patrimonio.getDataVencimento())
                .fornecedor(Objects.isNull(patrimonio.getFornecedor()) ? " " : formatarFornecedor(patrimonio.getFornecedor()))
                .contaContabil(Objects.isNull(patrimonio.getContaContabil()) ? " " : formatarContaContabil(patrimonio.getContaContabil()))
                .valorLiquido(patrimonio.getValorLiquido())
                .taxaAmortizada(patrimonio.getDadosAmortizacao())
                .build())));

        return dados;
    }

    private Map<Integer, Object> getMapItem(RelatorioListagemPatrimonios.Patrimonio patrimonio) {
        Map<Integer, Object> item = new HashMap<>();

        item.put(0, Objects.isNull(patrimonio.getNumero()) ? patrimonio.getId().toString() : Double.valueOf(patrimonio.getNumero()));
        item.put(1, patrimonio.getTipo());
        item.put(2, patrimonio.getNome());
        item.put(3, patrimonio.getOrgao());
        item.put(4, patrimonio.getSetor());
        item.put(5, patrimonio.getDescricao());
        item.put(6, patrimonio.getEstado());
        item.put(7, Objects.isNull(patrimonio.getValorAquisicao()) ? " " : patrimonio.getValorAquisicao().doubleValue());
        item.put(8, Objects.isNull(patrimonio.getValorEntrada()) ? " " : patrimonio.getValorEntrada().doubleValue());
        item.put(9, patrimonio.getReconhecimento());
        item.put(10, Objects.isNull(patrimonio.getDataAquisicao()) ? " " : patrimonio.getDataAquisicao().format(DateTimeFormatter.ofPattern(MASCARA_DATA)));
        item.put(11, Objects.isNull(patrimonio.getDataAtivacao()) ? " " : patrimonio.getDataAtivacao().format(DateTimeFormatter.ofPattern(MASCARA_DATA)));
        item.put(12, Objects.isNull(patrimonio.getNotaLancamentoContabil().getDataLancamento()) ? " " : patrimonio.getNotaLancamentoContabil().getDataLancamento().format(DateTimeFormatter.ofPattern(MASCARA_DATA)));
        item.put(13,Objects.isNull(patrimonio.getNotaLancamentoContabil().getNumero()) ? " " : patrimonio.getNotaLancamentoContabil().getNumero());
        item.put(14, patrimonio.getFornecedor());
        item.put(15, Objects.isNull(patrimonio.getVencimentoLicenca()) ? " " : patrimonio.getVencimentoLicenca().format(DateTimeFormatter.ofPattern(MASCARA_DATA)));
        item.put(16, patrimonio.getContaContabil());
        item.put(17, patrimonio.getSituacaoPatrimonio());
        item.put(18, Objects.isNull(patrimonio.getTaxaAmortizada()) ? " " : formatarTaxaAmortizacao(patrimonio.getTaxaAmortizada()));
        item.put(19, Objects.isNull(patrimonio.getValorLiquido()) ? " " : patrimonio.getValorLiquido().doubleValue());

        return item;
    }

    private double formatarTaxaAmortizacao(DadosAmortizacao dadosAmortizacao) {
        return dadosAmortizacao.getConfigAmortizacao().getTaxa().doubleValue() / BASE_PORCENTAGEM;
    }

    private String formatarOrgao(UnidadeOrganizacional orgao) {
        return orgao.getSigla() + " - " + orgao.getNome();
    }

    private String formatarFornecedor(Fornecedor fornecedor) {
        return fornecedor.getCpfCnpj() + " - " + fornecedor.getNome();
    }

    private String formatarContaContabil(ContaContabil contaContabil) {
        return contaContabil.getCodigo() + " - " + contaContabil.getDescricao();
    }

    private XLSFactory configurarRelatorio() {
        return XLSFactory.getInstance(COLUNAS.length, MASCARA_DATA, 0)
            .inserirCabecalhos(COLUNAS);
    }

    private Arquivo gerarRelatorio(XLSFactory xlsFactory, List<Map<Integer, Object>> dados) {

        ArrayList<String> typesCells = setTypesCells();

        try {
            xlsFactory = xlsFactory.inserirDadosNasCelulas(dados, typesCells)
                .autoSizeColumns(COLUNAS.length)
                .setSizeColumn(0, 6000)
                .setSizeColumn(1, 10000)
                .setSizeColumn(2, 6000)
                .setSizeColumn(3, 12000)
                .setSizeColumn(4, 12000)
                .setSizeColumn(5, 20000)
                .setSizeColumn(6, 6000)
                .setSizeColumn(7, 8000)
                .setSizeColumn(8, 8000)
                .setSizeColumn(9, 9000)
                .setSizeColumn(10, 6000)
                .setSizeColumn(11, 6000)
                .setSizeColumn(12, 6000)
                .setSizeColumn(13, 6000)
                .setSizeColumn(14, 10000)
                .setSizeColumn(15, 6000)
                .setSizeColumn(16, 15000)
                .setSizeColumn(17, 6000)
                .setSizeColumn(18, 8000)
                .setSizeColumn(19, 7000)
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

    private ArrayList<String> setTypesCells() {
        ArrayList<String> cellsTypes = new ArrayList<>();
        cellsTypes.add("0000000000");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("R$#,##0.00");
        cellsTypes.add("R$#,##0.00");
        cellsTypes.add("@");
        cellsTypes.add("dd/MM/yyyy");
        cellsTypes.add("dd/MM/yyyy");
        cellsTypes.add("dd/MM/yyyy");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("dd/MM/yyyy");
        cellsTypes.add("@");
        cellsTypes.add("@");
        cellsTypes.add("0.000%");
        cellsTypes.add("R$#,##0.00");
        return cellsTypes;
    }
}
