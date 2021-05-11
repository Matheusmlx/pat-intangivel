package br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Arquivo;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.RelatorioMemorandoMovimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.geraamrotizacaomensal.exception.RodarAmortizacaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.parametro.buscar.BuscarParametrosUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.converter.GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.memorando.gerarrelatoriomovimentacao.exception.LancamentoContabilNaoEncontradoException;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static br.com.azi.patrimoniointangivel.utils.string.StringUtils.formatString;

@AllArgsConstructor
public class GerarRelatorioMemorandoMovimentacaoPDFUseCaseImpl implements GerarRelatorioMemorandoMovimentacaoPDFUseCase{

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    private AmortizacaoDataProvider amortizacaoDataProvider;

    private GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase;

    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private GerarRelatorioMemorandoMovimentacaoPDFOutputDataConverter outputDataConverter;

    private BuscarParametrosUseCase buscarParametrosUseCase;

    @Override
    public GerarRelatorioMemorandoMovimentacaoPDFOutputData executar(GerarRelatorioMemorandoMovimentacaoPDFInputData inputData) {
        validarDadosEntrada(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData);

        verificaNumeroMemorando(movimentacao);

        RelatorioMemorandoMovimentacao relatorioMemorandoMovimentacao = prepararRelatorio(movimentacao);

        validarSituacaoMovimentacao(movimentacao,relatorioMemorandoMovimentacao);

        Arquivo arquivo = sistemaDeRelatoriosIntegration.gerarRelatorioMemorandoMovimentacaoPDF(relatorioMemorandoMovimentacao);


        return outputDataConverter.to(arquivo);
    }

    private void validarDadosEntrada(GerarRelatorioMemorandoMovimentacaoPDFInputData inputData){
        Validator.of(inputData)
            .validate(GerarRelatorioMemorandoMovimentacaoPDFInputData::getId, Objects::nonNull,"O id da movimentacao é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(GerarRelatorioMemorandoMovimentacaoPDFInputData inputData){
        Optional<Movimentacao> entidadeMovimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return entidadeMovimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void verificaNumeroMemorando(Movimentacao movimentacao){
        if(Objects.isNull(movimentacao.getAnoMemorando()) && Objects.isNull(movimentacao.getNumeroMemorando())){
            gerarNumeroMemorando(movimentacao);
            salvaMovimentacaoComMemorando(movimentacao);
        }
    }

    private RelatorioMemorandoMovimentacao prepararRelatorio(Movimentacao movimentacao) {
        if (existeAmortizacaoPorPatrimonio(movimentacao.getPatrimonio())) {
            if (movimentacao.getSituacao().equals(Movimentacao.Situacao.FINALIZADO)) {
                LancamentosContabeis lancamentoContabilOrigem = buscarLancamentoContabilPorMovimentacaoETipoLancamento(movimentacao, LancamentosContabeis.TipoLancamento.DEBITO);
                LancamentosContabeis lancamentoContabilAnterior = buscarLancamentoContabilAnteriorCredito(lancamentoContabilOrigem);
                if (verificaSeJaAmortizouNoPeriodo(lancamentoContabilOrigem) && !verificarSeHaLancamentosContabeisNoMesmoMesAno(lancamentoContabilOrigem, lancamentoContabilAnterior)) {
                    Amortizacao amortizacao = buscarPrimeiraAmortizacaoPorOrgao(lancamentoContabilOrigem);
                    return gerarRelatorioMovimentacaoFinalizadaComAmortizacao(movimentacao, amortizacao, lancamentoContabilOrigem);
                }
                if (!verificaSeJaAmortizouNoPeriodo(lancamentoContabilOrigem) && !verificarSeHaLancamentosContabeisNoMesmoMesAno(lancamentoContabilOrigem, lancamentoContabilAnterior)) {
                    return gerarRelatorioMovimentacaoFinalizadaComUltimaContaContabil(movimentacao, lancamentoContabilOrigem, lancamentoContabilAnterior);
                }

                return gerarRelatorioMovimentacaoFinalizadaComContaContabil(movimentacao, lancamentoContabilOrigem);
            }
            Amortizacao amortizacao = buscarUltimaAmortizacao(movimentacao.getPatrimonio());
            return gerarRelatorioMovimentacaoAguardandoRecebimentoComAmortizacao(movimentacao, amortizacao);
        }
        return gerarRelatorioMovimentacaoSemAmortizacao(movimentacao);
    }

    private Boolean verificarSeHaLancamentosContabeisNoMesmoMesAno(LancamentosContabeis lancamentoContabilOrigem, LancamentosContabeis lancamentoContabilAnterior) {
        return DateUtils.mesmoMesAno(lancamentoContabilOrigem.getDataLancamento(), lancamentoContabilAnterior.getDataLancamento());
    }

    private void validarSituacaoMovimentacao(Movimentacao movimentacao, RelatorioMemorandoMovimentacao relatorioMemorandoMovimentacao) {
        if (movimentacao.getSituacao().equals(Movimentacao.Situacao.EM_ELABORACAO) || movimentacao.getSituacao().equals(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO)) {
            setarMensagemRodape(relatorioMemorandoMovimentacao);
        }
    }

    private void setarMensagemRodape(RelatorioMemorandoMovimentacao relatorioMemorandoMovimentacao) {
        BuscarParametrosOutputData outputData = buscarParametrosUseCase.executar();
        relatorioMemorandoMovimentacao.setMensagemRodapeMemorandoMovimentacao(outputData.getMensagemRodapeMemorandoMovimentacao());
    }

    private boolean existeAmortizacaoPorPatrimonio(Patrimonio patrimonio) {
        return amortizacaoDataProvider.existePorPatrimonio(patrimonio.getId());
    }

    private LancamentosContabeis buscarLancamentoContabilPorMovimentacaoETipoLancamento(Movimentacao movimentacao, LancamentosContabeis.TipoLancamento tipoLancamento) {
        Optional<LancamentosContabeis> lancamentoContabil = lancamentosContabeisDataProvider.buscarPorMovimentacaoETipoLancamento(movimentacao.getId(), tipoLancamento.name());
        return lancamentoContabil.orElseThrow(LancamentoContabilNaoEncontradoException::new);
    }

    private Boolean verificaSeJaAmortizouNoPeriodo(LancamentosContabeis lancamentosContabeis) {
        return amortizacaoDataProvider.existePorPatrimonioEOrgaoNoPeriodo(lancamentosContabeis.getPatrimonio().getId(), lancamentosContabeis.getOrgao().getId(), lancamentosContabeis.getDataLancamento());
    }

    private LancamentosContabeis buscarLancamentoContabilAnteriorCredito(LancamentosContabeis lancamentosContabeis) {
        Optional<LancamentosContabeis> lancamentoContabil = lancamentosContabeisDataProvider.buscarLancamentoContabilAnteriorCredito(lancamentosContabeis.getPatrimonio().getId(), lancamentosContabeis.getOrgao().getId(), lancamentosContabeis.getDataLancamento());
        return lancamentoContabil.orElseThrow(LancamentoContabilNaoEncontradoException::new);
    }

    private Amortizacao buscarPrimeiraAmortizacaoPorOrgao(LancamentosContabeis lancamentosContabeis) {
        Optional<Amortizacao> amortizacao = amortizacaoDataProvider.buscarPrimeiraPorOrgaoEPatrimonio(lancamentosContabeis.getOrgao().getId(), lancamentosContabeis.getPatrimonio().getId());
        return amortizacao.orElseThrow(RodarAmortizacaoException::new);
    }

    private Amortizacao buscarUltimaAmortizacao(Patrimonio patrimonio) {
        Optional<Amortizacao> ultimaAmortizacao = amortizacaoDataProvider.buscarUltimaPorPatrimonio(patrimonio.getId());
        return ultimaAmortizacao.orElseThrow(RodarAmortizacaoException::new);
    }

    private RelatorioMemorandoMovimentacao gerarRelatorioMovimentacaoFinalizadaComAmortizacao(Movimentacao movimentacao, Amortizacao amortizacao, LancamentosContabeis lancamentosContabeis) {
        return RelatorioMemorandoMovimentacao.builder()
            .numeroAno(movimentacao.getNumeroMemorando() + "/" + movimentacao.getAnoMemorando())
            .mesAno(criaDataRelatorio(movimentacao.getDataDeFinalizacao()))
            .movimentacao(RelatorioMemorandoMovimentacao.Movimentacao.builder()
                .codigo(movimentacao.getCodigo())
                .tipo(movimentacao.getTipo().getValor())
                .dataEnvio(retornaDataString(movimentacao.getDataDeEnvio()))
                .dataRecebido(retornaDataString(movimentacao.getDataDeFinalizacao()))
                .motivo(movimentacao.getMotivo())
                .build())
            .contaContabil(formatarContaContabil(movimentacao.getPatrimonio().getContaContabil()))
            .patrimonio(RelatorioMemorandoMovimentacao.Patrimonio.builder()
                .numero(movimentacao.getPatrimonio().getNumero())
                .nome(movimentacao.getPatrimonio().getNome())
                .tipo(movimentacao.getPatrimonio().getTipo().getValor())
                .valorLiquido(lancamentosContabeis.getValorLiquido())
                .valorAquisicao(movimentacao.getPatrimonio().getValorAquisicao())
                .valorEntrada(amortizacao.getValorAnterior())
                .valorAmortizadoAcumulado(amortizacao.getValorAnterior().subtract(lancamentosContabeis.getValorLiquido()))
                .build())
            .orgaoOrigem(formatarOrgao(movimentacao.getOrgaoOrigem()))
            .orgaoDestino(formatarOrgao(movimentacao.getOrgaoDestino()))
            .build();
    }

    private RelatorioMemorandoMovimentacao gerarRelatorioMovimentacaoFinalizadaComContaContabil(Movimentacao movimentacao, LancamentosContabeis lancamentosContabeis) {
        return RelatorioMemorandoMovimentacao.builder()
            .numeroAno(movimentacao.getNumeroMemorando() + "/" + movimentacao.getAnoMemorando())
            .mesAno(criaDataRelatorio(movimentacao.getDataDeFinalizacao()))
            .movimentacao(RelatorioMemorandoMovimentacao.Movimentacao.builder()
                .codigo(movimentacao.getCodigo())
                .tipo(movimentacao.getTipo().getValor())
                .dataEnvio(retornaDataString(movimentacao.getDataDeEnvio()))
                .dataRecebido(retornaDataString(movimentacao.getDataDeFinalizacao()))
                .motivo(movimentacao.getMotivo())
                .build())
            .contaContabil(formatarContaContabil(movimentacao.getPatrimonio().getContaContabil()))
            .patrimonio(RelatorioMemorandoMovimentacao.Patrimonio.builder()
                .numero(movimentacao.getPatrimonio().getNumero())
                .nome(movimentacao.getPatrimonio().getNome())
                .tipo(movimentacao.getPatrimonio().getTipo().getValor())
                .valorLiquido(lancamentosContabeis.getValorLiquido())
                .valorAquisicao(movimentacao.getPatrimonio().getValorAquisicao())
                .valorEntrada(lancamentosContabeis.getValorLiquido())
                .valorAmortizadoAcumulado(lancamentosContabeis.getValorLiquido().subtract(lancamentosContabeis.getValorLiquido()))
                .build())
            .orgaoOrigem(formatarOrgao(movimentacao.getOrgaoOrigem()))
            .orgaoDestino(formatarOrgao(movimentacao.getOrgaoDestino()))
            .build();
    }

    private RelatorioMemorandoMovimentacao gerarRelatorioMovimentacaoFinalizadaComUltimaContaContabil(Movimentacao movimentacao, LancamentosContabeis lancamentoContabilOrigem, LancamentosContabeis lancamentoContabilAnterior) {
        return RelatorioMemorandoMovimentacao.builder()
            .numeroAno(movimentacao.getNumeroMemorando() + "/" + movimentacao.getAnoMemorando())
            .mesAno(criaDataRelatorio(movimentacao.getDataDeFinalizacao()))
            .movimentacao(RelatorioMemorandoMovimentacao.Movimentacao.builder()
                .codigo(movimentacao.getCodigo())
                .tipo(movimentacao.getTipo().getValor())
                .dataEnvio(retornaDataString(movimentacao.getDataDeEnvio()))
                .dataRecebido(retornaDataString(movimentacao.getDataDeFinalizacao()))
                .motivo(movimentacao.getMotivo())
                .build())
            .contaContabil(formatarContaContabil(movimentacao.getPatrimonio().getContaContabil()))
            .patrimonio(RelatorioMemorandoMovimentacao.Patrimonio.builder()
                .numero(movimentacao.getPatrimonio().getNumero())
                .nome(movimentacao.getPatrimonio().getNome())
                .tipo(movimentacao.getPatrimonio().getTipo().getValor())
                .valorLiquido(lancamentoContabilOrigem.getValorLiquido())
                .valorAquisicao(movimentacao.getPatrimonio().getValorAquisicao())
                .valorEntrada(lancamentoContabilAnterior.getValorLiquido())
                .valorAmortizadoAcumulado(lancamentoContabilAnterior.getValorLiquido().subtract(lancamentoContabilOrigem.getValorLiquido()))
                .build())
            .orgaoOrigem(formatarOrgao(movimentacao.getOrgaoOrigem()))
            .orgaoDestino(formatarOrgao(movimentacao.getOrgaoDestino()))
            .build();
    }

    private RelatorioMemorandoMovimentacao gerarRelatorioMovimentacaoAguardandoRecebimentoComAmortizacao(Movimentacao movimentacao, Amortizacao amortizacao) {
        return RelatorioMemorandoMovimentacao.builder()
            .numeroAno(movimentacao.getNumeroMemorando() + "/" + movimentacao.getAnoMemorando())
            .mesAno(Objects.nonNull(movimentacao.getDataDeFinalizacao()) ? criaDataRelatorio(movimentacao.getDataDeFinalizacao()) : " ")
            .movimentacao(RelatorioMemorandoMovimentacao.Movimentacao.builder()
                .codigo(movimentacao.getCodigo())
                .tipo(movimentacao.getTipo().getValor())
                .dataEnvio(retornaDataString(movimentacao.getDataDeEnvio()))
                .dataRecebido(retornaDataString(movimentacao.getDataDeFinalizacao()))
                .motivo(movimentacao.getMotivo())
                .build())
            .contaContabil(formatarContaContabil(movimentacao.getPatrimonio().getContaContabil()))
            .patrimonio(RelatorioMemorandoMovimentacao.Patrimonio.builder()
                .numero(movimentacao.getPatrimonio().getNumero())
                .nome(movimentacao.getPatrimonio().getNome())
                .tipo(movimentacao.getPatrimonio().getTipo().getValor())
                .valorLiquido(amortizacao.getValorPosterior())
                .valorAquisicao(movimentacao.getPatrimonio().getValorAquisicao())
                .valorEntrada(movimentacao.getPatrimonio().getValorEntrada())
                .valorAmortizadoAcumulado(movimentacao.getPatrimonio().getValorEntrada().subtract(amortizacao.getValorPosterior()))
                .build())
            .orgaoOrigem(formatarOrgao(movimentacao.getOrgaoOrigem()))
            .orgaoDestino(formatarOrgao(movimentacao.getOrgaoDestino()))
            .build();
    }

    private RelatorioMemorandoMovimentacao gerarRelatorioMovimentacaoSemAmortizacao(Movimentacao movimentacao) {
        return RelatorioMemorandoMovimentacao.builder()
            .numeroAno(movimentacao.getNumeroMemorando() + "/" + movimentacao.getAnoMemorando())
            .mesAno(Objects.nonNull(movimentacao.getDataDeFinalizacao()) ? criaDataRelatorio(movimentacao.getDataDeFinalizacao()) : " ")
            .movimentacao(RelatorioMemorandoMovimentacao.Movimentacao.builder()
                .codigo(movimentacao.getCodigo())
                .tipo(movimentacao.getTipo().getValor())
                .dataEnvio(retornaDataString(movimentacao.getDataDeEnvio()))
                .dataRecebido(retornaDataString(movimentacao.getDataDeFinalizacao()))
                .motivo(movimentacao.getMotivo())
                .build())
            .contaContabil(formatarContaContabil(movimentacao.getPatrimonio().getContaContabil()))
            .patrimonio(RelatorioMemorandoMovimentacao.Patrimonio.builder()
                .numero(movimentacao.getPatrimonio().getNumero())
                .nome(movimentacao.getPatrimonio().getNome())
                .tipo(movimentacao.getPatrimonio().getTipo().getValor())
                .valorLiquido(movimentacao.getPatrimonio().getValorLiquido())
                .valorAquisicao(movimentacao.getPatrimonio().getValorAquisicao())
                .valorEntrada(movimentacao.getPatrimonio().getValorEntrada())
                .valorAmortizadoAcumulado(movimentacao.getPatrimonio().getValorAquisicao().subtract(movimentacao.getPatrimonio().getValorLiquido()))
                .build())
            .orgaoOrigem(formatarOrgao(movimentacao.getOrgaoOrigem()))
            .orgaoDestino(formatarOrgao(movimentacao.getOrgaoDestino()))
            .build();
    }

    private String criaDataRelatorio(LocalDateTime data){
        String mes = data.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt"));
        String ano = String.valueOf(data.getYear());
        return StringUtils.capitalize(mes)+ "/" + ano;
    }

    private String formatarContaContabil(ContaContabil contaContabil){
        if(Objects.nonNull(contaContabil)){
            return formatString(contaContabil.getCodigo(),"##.###.##.##") + " - " + contaContabil.getDescricao();
        }
        return "-";
    }

    private String formatarOrgao(UnidadeOrganizacional orgao){
        if(Objects.nonNull(orgao)){
            return orgao.getNome() + " - " + orgao.getSigla();
        }
        return "-";
    }

    private String retornaDataString(LocalDateTime data){
        if(Objects.nonNull(data)){
            return data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear();
        }
        return "-";
    }
    private void gerarNumeroMemorando(Movimentacao movimentacao){
        GerarNumeroMemorandoOutputData gerarNumeroMemorandoOutputData = gerarNumeroMemorandoUseCase.executar();
        movimentacao.setNumeroMemorando(gerarNumeroMemorandoOutputData.getNumeroMemorando());
        movimentacao.setAnoMemorando(Calendar.getInstance().get(Calendar.YEAR));
    }

    private void salvaMovimentacaoComMemorando(Movimentacao movimentacao){
        movimentacaoDataProvider.salvar(movimentacao);
    }
}
