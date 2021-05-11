package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar;

import br.com.azi.patrimoniointangivel.domain.entity.Amortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.enviar.exception.MovimentarPatrimonioComAmortizacaoPendenteException;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

@AllArgsConstructor
public class EnviarMovimentacaoUseCaseImpl  implements EnviarMovimentacaoUseCase {

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private AmortizacaoDataProvider amortizacaoDataProvider;

    private Clock clock;

    @Override
    public void executar(EnviarMovimentacaoInputData inputData) {
        validarSeMovimentacaoExiste(inputData);
        Movimentacao movimentacao = buscar(inputData);
        validarSeExisteAmortizacaoPendente(movimentacao.getPatrimonio());

        registrarDataHoraDeEnvio(movimentacao);
        atualizarSituacao(movimentacao);

        salvar(movimentacao);
    }

    private void validarSeMovimentacaoExiste(EnviarMovimentacaoInputData inputData){
        if(!movimentacaoDataProvider.existe(inputData.getId())){
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscar(EnviarMovimentacaoInputData inputData){
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return (movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new));
    }

    private void  validarSeExisteAmortizacaoPendente(Patrimonio patrimonio) {
        if (!validarSePatrimonioAtivadoNoMesCorrente(patrimonio) && patrimonio.getAmortizavel()) {
            LocalDateTime mesAnterior = YearMonth.from(LocalDateTime.now(clock).minusMonths(1)).atEndOfMonth().atTime(23, 59, 59);
            Amortizacao ultimaAmortizacao = buscarUltimaAmortizacao(patrimonio);
            if (validarSePatrimonioJaVenceu(patrimonio) && !DateUtils.mesmoMesAno(ultimaAmortizacao.getDataFinal(), patrimonio.getFimVidaUtil())) {
                throw new MovimentarPatrimonioComAmortizacaoPendenteException();
            }
            if (!validarSePatrimonioJaVenceu(patrimonio) && !DateUtils.mesmoMesAno(ultimaAmortizacao.getDataFinal(), mesAnterior)) {
                throw new MovimentarPatrimonioComAmortizacaoPendenteException();
            }
        }
    }

    private boolean validarSePatrimonioAtivadoNoMesCorrente(Patrimonio patrimonio) {
        return DateUtils.mesmoMesAno(patrimonio.getDataAtivacao(), LocalDateTime.now());
    }

    private Amortizacao buscarUltimaAmortizacao(Patrimonio patrimonio) {
        Optional<Amortizacao> ultimaAmortizacao = amortizacaoDataProvider.buscarUltimaPorPatrimonio(patrimonio.getId());
        return ultimaAmortizacao.orElseThrow(MovimentarPatrimonioComAmortizacaoPendenteException::new);
    }

    private Boolean validarSePatrimonioJaVenceu(Patrimonio patrimonio){
        return patrimonio.getFimVidaUtil().isBefore(LocalDateTime.now());
    }

    private void registrarDataHoraDeEnvio(Movimentacao movimentacao) {
        movimentacao.setDataDeEnvio(LocalDateTime.now(clock));
    }

    private void atualizarSituacao(Movimentacao movimentacao) {
        movimentacao.setSituacao(Movimentacao.Situacao.AGUARDANDO_RECEBIMENTO);
    }

    private void salvar(Movimentacao movimentacao){
        movimentacaoDataProvider.salvar(movimentacao);
    }
}
