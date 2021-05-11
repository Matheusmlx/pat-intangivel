package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoInputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.salvardadosamortizacao.SalvaConfigAmortizacaoUseCase;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.converter.AtivarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception.AtivarPatrimonioException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception.DataAtivacaoIgualDataDeVencimentoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.exception.SelecionarTipoAmortizacaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.patrimonio.GerarNumeroPatrimonioUseCase;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class AtivarPatrimonioUseCaseImpl implements AtivarPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private AtivarPatrimonioOutputDataConverter outputDataConverter;

    private Short vidautil;

    private GerarNumeroPatrimonioUseCase gerarNumeroPatrimonioUseCase;

    private SalvaConfigAmortizacaoUseCase salvaConfigAmortizacaoUseCase;

    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    private ContaContabilDataProvider contaContabilDataProvider;

    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    private String dataCorteInicioCadastroRetroativo;

    @Override
    public AtivarPatrimonioOutputData executar(AtivarPatrimonioInputData inputData) {
        validarDadosEntrada(inputData);

        Patrimonio patrimonio = buscar(inputData);
        validarCamposObrigatorios(patrimonio);
        verificarDataAtivacao(patrimonio);
        verificarDataLimite(patrimonio);

        setarCampos(patrimonio);
        gerarNumeroPatrimonio(patrimonio);
        calcularVidaUtil(patrimonio);
        calcularDataFimVidaUtil(patrimonio);
        verificarAtivacaoRetroativa(patrimonio);
        setarPatrimonioAmortizavel(patrimonio);

        LancamentosContabeis lancamentosContabeis = gerarLancamentoContabilCredito(patrimonio);
        salvarLancamentoContabil(lancamentosContabeis);

        if (patrimonio.getAmortizavel()) {
            validarDataVencimento(patrimonio);
            ConfigAmortizacao configAmortizacao = gerarConfigAmortizacao(patrimonio);
            salvarDadosAmortizacao(patrimonio, configAmortizacao);
        }

        Patrimonio patrimonioAtivo = atualizar(patrimonio);

        return outputDataConverter.to(patrimonioAtivo);
    }

    private void validarDadosEntrada(AtivarPatrimonioInputData inputData) {
        Validator.of(inputData)
            .validate(AtivarPatrimonioInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private Patrimonio buscar(AtivarPatrimonioInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getId());
        return patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private void validarDataVencimento(Patrimonio patrimonio) {
        if (patrimonio.getReconhecimento().equals(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)) {
            validarSeDataDeAtivacaoIgualDataVencimento(patrimonio);
            validarSeDataVencimentoPosteriorDataAtivacao(patrimonio);
        }
    }

    private void validarSeDataDeAtivacaoIgualDataVencimento(Patrimonio patrimonio){
        if(DateUtils.mesmoDiaMesAno(patrimonio.getDataAtivacao(), patrimonio.getDataVencimento())){
            throw  new DataAtivacaoIgualDataDeVencimentoException();
        }
    }

    private void validarSeDataVencimentoPosteriorDataAtivacao(Patrimonio patrimonio) {
        if (patrimonio.getDataVencimento().isBefore(patrimonio.getDataAtivacao())) {
            throw  new DataAtivacaoIgualDataDeVencimentoException();
        }
    }

    private void validarCamposObrigatorios(Patrimonio patrimonio) {
        Validator.of(patrimonio)
            .validate(Patrimonio::getOrgao, Objects::nonNull, "O órgão é nulo")
            .validate(Patrimonio::getSituacao, Objects::nonNull, "A situação é nula")
            .validate(Patrimonio::getValorAquisicao, Objects::nonNull, "O valor de aquisição é nulo")
            .validate(Patrimonio::getReconhecimento, Objects::nonNull, "O tipo de reconhecimento é nulo")
            .get();
    }

    private void verificarDataAtivacao(Patrimonio patrimonio) {
        if (Objects.isNull(patrimonio.getDataAtivacao())){
            patrimonio.setDataAtivacao(LocalDateTime.now());
        }
    }

    private void verificarDataLimite(Patrimonio patrimonio) {
        if (Objects.nonNull(dataCorteInicioCadastroRetroativo) &&
            patrimonio.getDataAtivacao().isBefore(DateUtils.converterStringParaLocalDateTime(dataCorteInicioCadastroRetroativo))) {
            throw new AtivarPatrimonioException();
        }
    }

    private void setarCampos(Patrimonio patrimonio) {
        patrimonio.setDataFinalAtivacao(LocalDateTime.now());
        patrimonio.setInicioVidaUtil(patrimonio.getDataAtivacao());
        patrimonio.setSituacao(Patrimonio.Situacao.ATIVO);
        patrimonio.setValorLiquido(patrimonio.getValorAquisicao());
        patrimonio.setValorEntrada(patrimonio.getValorAquisicao());
    }

    private void gerarNumeroPatrimonio(Patrimonio patrimonio) {
        GerarNumeroPatrimonioOutputData gerarNumeroPatrimonioOutputData = gerarNumeroPatrimonioUseCase.executar();
        patrimonio.setNumero(gerarNumeroPatrimonioOutputData.getNumero());
    }

    private void calcularVidaUtil(Patrimonio patrimonio) {
        if (!patrimonio.getVidaIndefinida()) {
            if (Objects.nonNull(patrimonio.getDataVencimento())) {
                patrimonio.setMesesVidaUtil((short) ChronoUnit.MONTHS.between(patrimonio.getDataAtivacao().withDayOfMonth(1), patrimonio.getDataVencimento().plusMonths(1)));
            } else {
                patrimonio.setMesesVidaUtil(vidautil);
            }
        }
    }

    private void calcularDataFimVidaUtil(Patrimonio patrimonio) {
        if (!patrimonio.getVidaIndefinida()) {
            if (Objects.nonNull(patrimonio.getDataVencimento())) {
                if (patrimonio.getReconhecimento().equals(Patrimonio.Reconhecimento.AQUISICAO_SEPARADA)) {
                    patrimonio.setFimVidaUtil(patrimonio.getDataVencimento());
                }
            } else {
                patrimonio.setFimVidaUtil(LocalDateTime
                    .from(patrimonio.getDataAtivacao())
                        .plusMonths(patrimonio.getMesesVidaUtil())
                            .toLocalDate().atTime(23,59,59)
                                .minusDays(1));
            }
        }
    }

    private void verificarAtivacaoRetroativa(Patrimonio patrimonio) {
        if (patrimonio.getDataAtivacao().isBefore(LocalDate.now().atStartOfDay())) {
            patrimonio.setAtivacaoRetroativa(Boolean.TRUE);
        }else {
            patrimonio.setAtivacaoRetroativa(Boolean.FALSE);
        }
    }

    private Boolean verificarContaContabilAmortizavel(Patrimonio patrimonio) {
        ContaContabil contaContabil = buscaContaContabil(patrimonio.getContaContabil().getId());
        ConfigContaContabil configContaContabil = buscaConfigContaContabil(contaContabil);
        return configContaContabil.getTipo().equals(ConfigContaContabil.Tipo.AMORTIZAVEL);
    }

    private ConfigContaContabil buscaConfigContaContabil(ContaContabil contaContabil) {
        Optional<ConfigContaContabil> configContaContabil = configContaContabilDataProvider.buscarAtualPorContaContabil(contaContabil.getId());
        return configContaContabil.orElseThrow(SelecionarTipoAmortizacaoException::new);
    }

    private ContaContabil buscaContaContabil(Long contaContabilId) {
        Optional<ContaContabil> contaContabil = contaContabilDataProvider.buscarPorId(contaContabilId);
        return contaContabil.orElseThrow(AtivarPatrimonioException::new);
    }

    private void setarPatrimonioAmortizavel(Patrimonio patrimonio) {
        if (!patrimonio.getVidaIndefinida() && verificarContaContabilAmortizavel(patrimonio)){
            patrimonio.setAmortizavel(Boolean.TRUE);
        }else {
            patrimonio.setAmortizavel(Boolean.FALSE);
        }
    }

    private LancamentosContabeis gerarLancamentoContabilCredito(Patrimonio patrimonio) {
        return LancamentosContabeis
            .builder()
            .patrimonio(patrimonio)
            .orgao(patrimonio.getOrgao())
            .contaContabil(patrimonio.getContaContabil())
            .tipoLancamento(LancamentosContabeis.TipoLancamento.CREDITO)
            .motivoLancamento(LancamentosContabeis.MotivoLancamento.ATIVACAO)
            .valorLiquido(patrimonio.getValorLiquido())
            .dataLancamento(patrimonio.getDataAtivacao())
            .build();
    }

    private void salvarLancamentoContabil(LancamentosContabeis lancamentosContabeis){
        lancamentosContabeisDataProvider.salvar(lancamentosContabeis);
    }

    private ConfigAmortizacao gerarConfigAmortizacao(Patrimonio patrimonio) {
        SalvaConfigAmortizacaoInputData input = SalvaConfigAmortizacaoInputData
            .builder()
            .vidaUtil(patrimonio.getMesesVidaUtil())
            .tipo(ConfigAmortizacao.Tipo.AMORTIZAVEL.name())
            .contaContabil(patrimonio.getContaContabil().getId())
            .build();

        SalvaConfigAmortizacaoOutputData salvaConfigAmortizacaoOutputData = salvaConfigAmortizacaoUseCase.executar(input);

        return ConfigAmortizacao
            .builder()
            .id(salvaConfigAmortizacaoOutputData.getId())
            .metodo(ConfigAmortizacao.Metodo.valueOf(salvaConfigAmortizacaoOutputData.getMetodo()))
            .vidaUtil(salvaConfigAmortizacaoOutputData.getVidaUtil())
            .situacao(ConfigAmortizacao.Situacao.valueOf(salvaConfigAmortizacaoOutputData.getSituacao()))
            .taxa(salvaConfigAmortizacaoOutputData.getTaxa())
            .percentualResidual(salvaConfigAmortizacaoOutputData.getPercentualResidual())
            .build();
    }

    private void salvarDadosAmortizacao(Patrimonio patrimonio, ConfigAmortizacao configAmortizacao) {
        DadosAmortizacao dadosAmortizacao = DadosAmortizacao
            .builder()
            .patrimonio(patrimonio)
            .configAmortizacao(configAmortizacao)
            .build();
        DadosAmortizacao dadosAmortizacaoSalvo = dadosAmortizacaoDataProvider.salvar(dadosAmortizacao);
        patrimonio.setDadosAmortizacao(dadosAmortizacaoSalvo);
    }

    private Patrimonio atualizar(Patrimonio patrimonio) {
        return patrimonioDataProvider.atualizar(patrimonio);
    }
}
