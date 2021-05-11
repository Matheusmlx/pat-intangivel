package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico;

import br.com.azi.patrimoniointangivel.domain.constant.enums.TipoRelatorio;
import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.Job;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeisAgrupado;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioSintetico;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.RelatorioSinteticoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UnidadeOrganizacionalDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.converter.GerarInventarioSinteticoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception.AmortizacaoEmAndamentoException;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception.BuscaRelatorioException;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception.OrgaoNaoEncontradoException;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class GerarInventarioSinteticoUseCaseImpl implements GerarInventarioSinteticoUseCase {

    private RelatorioSinteticoDataProvider relatorioDataProvider;

    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private UnidadeOrganizacionalDataProvider unidadeOrganizacionalDataProvider;

    private GerarInventarioSinteticoOutputDataConverter outputDataConverter;

    @Override
    public GerarInventarioSinteticoOutputData executar(GerarInventarioSinteticoInputData inputData) {
        validaAmortizacaoEmAndamento();
        validaDadosEntrada(inputData);
        LocalDateTime dataFinal = montaDataReferenciaRelatorio(inputData);

        UnidadeOrganizacional orgaoGerarRelatorio = buscarOrgaoRelatorio(inputData.getOrgao());
        List<RelatorioSintetico> relatorio = buscarRelatorio(inputData.getOrgao(), dataFinal);
        List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado = buscarLancamentosContabeisAgrupados(dataFinal);

        relatorio = filtrarRelatorioPorDataDoLancamentoContabil(relatorio, lancamentosContabeisAgrupado);
        List<RelatorioSintetico> relatorioFinal = injetarPatrimoniosQueNuncaForamAmortizados(relatorio, lancamentosContabeisAgrupado, inputData.getOrgao());

        validarSeExistemRegistrosParaGerarRelatorio(relatorioFinal);

        Arquivo arquivo = gerarArquivoRelatorio(inputData, relatorioFinal, lancamentosContabeisAgrupado, orgaoGerarRelatorio, dataFinal);

        return outputDataConverter.to(arquivo);
    }

    private List<RelatorioSintetico> injetarPatrimoniosQueNuncaForamAmortizados(List<RelatorioSintetico> relatorios, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupados
        , Long idOrgao) {
        boolean existePrimeiraAmortizacaoNoOrgao;
        for (LancamentosContabeisAgrupado lancamentosContabeisAgrupado : lancamentosContabeisAgrupados) {
            existePrimeiraAmortizacaoNoOrgao = false;
            for (RelatorioSintetico relatorio : relatorios) {
                if (validarSePrimeiraAmortizacaoDoPatrimonioNoOrgao(relatorio, lancamentosContabeisAgrupado)) {
                    existePrimeiraAmortizacaoNoOrgao = true;
                    break;
                }
            }
            if (!existePrimeiraAmortizacaoNoOrgao && lancamentosContabeisAgrupado.getOrgao().getId().equals(idOrgao)) {
                relatorios.add(
                    RelatorioSintetico.builder()
                        .patrimonioId(lancamentosContabeisAgrupado.getPatrimonio().getId())
                        .contaContabil(lancamentosContabeisAgrupado.getPatrimonio().getContaContabil())
                        .orgao(lancamentosContabeisAgrupado.getOrgao())
                        .dataAtivacao(lancamentosContabeisAgrupado.getMaiorData())
                        .valorLiquido(lancamentosContabeisAgrupado.getMaiorValorLiquido())
                        .valorAquisicao(lancamentosContabeisAgrupado.getMaiorValorLiquido())
                        .valorAmortizadoMensal(BigDecimal.valueOf(0))
                        .valorAmortizadoAcumulado(BigDecimal.valueOf(0))
                        .build()
                );
            }
        }
        return relatorios;
    }

    private boolean validarSePrimeiraAmortizacaoDoPatrimonioNoOrgao(RelatorioSintetico relatorio, LancamentosContabeisAgrupado lancamentosContabeisAgrupado) {
        return verificarSeOsPatrimoniosSaoOsMesmos(relatorio, lancamentosContabeisAgrupado) &&
            verificarSeOsOrgaosSaoOsMesmo(relatorio, lancamentosContabeisAgrupado) &&
            DateUtils.mesmoMesAno(lancamentosContabeisAgrupado.getMaiorData(), relatorio.getDataCadastro());
    }

    private List<RelatorioSintetico> filtrarRelatorioPorDataDoLancamentoContabil(List<RelatorioSintetico> relatorios, List<LancamentosContabeisAgrupado> lancamentosContabeisAgupados) {
        List<RelatorioSintetico> relatorioFiltrado = new ArrayList<>();

        relatorios.forEach(relatorio -> {
            lancamentosContabeisAgupados.forEach(lancamentosContabeisAgrupado -> {
                if (verificarSeOsPatrimoniosSaoOsMesmos(relatorio, lancamentosContabeisAgrupado) &&
                    verificarSeOsOrgaosSaoOsMesmo(relatorio, lancamentosContabeisAgrupado) &&
                    verificaSeAmortizacaoPosteriorAoLancamentoContabil(relatorio, lancamentosContabeisAgrupado)) {
                    relatorioFiltrado.add(relatorio);
                }
            });
        });
        return relatorioFiltrado;
    }

    private Arquivo gerarArquivoRelatorio(GerarInventarioSinteticoInputData inputData, List<RelatorioSintetico> registros, List<LancamentosContabeisAgrupado> lancamentosContabeisAgrupado, UnidadeOrganizacional orgaoRelatorio, LocalDateTime dataFinal) {
        if (inputData.getFormato().equals(TipoRelatorio.XLS.name())) {
            return sistemaDeRelatoriosIntegration.gerarRelatorioInventarioSinteticoXLS(registros, lancamentosContabeisAgrupado, orgaoRelatorio, dataFinal);
        }
        return sistemaDeRelatoriosIntegration.gerarRelatorioInventarioSinteticoPDF(registros, lancamentosContabeisAgrupado, orgaoRelatorio, dataFinal);
    }

    private void validarSeExistemRegistrosParaGerarRelatorio(List<RelatorioSintetico> relatorioFiltrado) {
        if (relatorioFiltrado.isEmpty()) {
            throw new BuscaRelatorioException();
        }
    }

    private Boolean verificarSeOsPatrimoniosSaoOsMesmos(RelatorioSintetico relatorioSintetico, LancamentosContabeisAgrupado lancamentosContabeisAgrupado) {
        return lancamentosContabeisAgrupado.getPatrimonio().getId().equals(relatorioSintetico.getPatrimonioId());
    }

    private Boolean verificarSeOsOrgaosSaoOsMesmo(RelatorioSintetico relatorioSintetico, LancamentosContabeisAgrupado lancamentosContabeisAgrupado) {
        return lancamentosContabeisAgrupado.getOrgao().getId().equals(relatorioSintetico.getOrgaoAmortizacao().getId());
    }

    private Boolean verificaSeAmortizacaoPosteriorAoLancamentoContabil(RelatorioSintetico relatorioSintetico, LancamentosContabeisAgrupado lancamentosContabeisAgrupado) {
        return relatorioSintetico.getDataCadastro().isAfter(lancamentosContabeisAgrupado.getMaiorData());
    }

    private void validaAmortizacaoEmAndamento() {
        Job job = Job.getInstance();
        if (job.getStatus().equals(Job.Status.EM_ANDAMENTO)) {
            throw new AmortizacaoEmAndamentoException();
        }
    }

    private void validaDadosEntrada(GerarInventarioSinteticoInputData inputData) {
        Validator.of(inputData)
            .validate(GerarInventarioSinteticoInputData::getOrgao, Objects::nonNull, "O id do órgão é nulo.")
            .validate(GerarInventarioSinteticoInputData::getMesReferencia, Objects::nonNull, "O mês de referência é nulo.")
            .get();
    }

    private LocalDateTime montaDataReferenciaRelatorio(GerarInventarioSinteticoInputData inputData) {
        String[] dataInput = inputData.getMesReferencia().split("-");
        int ano = Integer.parseInt(dataInput[0]);
        int mes = Integer.parseInt(dataInput[1]);
        return YearMonth.from(LocalDate.of(ano, mes, 1)).atEndOfMonth().atTime(23, 59, 59);
    }

    private List<RelatorioSintetico> buscarRelatorio(Long orgao, LocalDateTime dataFinal) {
        return relatorioDataProvider.buscaRelatorioSintetico(orgao, dataFinal);
    }

    private List<LancamentosContabeisAgrupado> buscarLancamentosContabeisAgrupados(LocalDateTime dataFinal) {
        return lancamentosContabeisDataProvider.buscarLancamentosContabeisAgrupados(dataFinal);
    }

    private UnidadeOrganizacional buscarOrgaoRelatorio(Long idOrgao) {
        Optional<UnidadeOrganizacional> orgao = unidadeOrganizacionalDataProvider.buscarPorId(idOrgao);
        return orgao.orElseThrow(OrgaoNaoEncontradoException::new);
    }
}
