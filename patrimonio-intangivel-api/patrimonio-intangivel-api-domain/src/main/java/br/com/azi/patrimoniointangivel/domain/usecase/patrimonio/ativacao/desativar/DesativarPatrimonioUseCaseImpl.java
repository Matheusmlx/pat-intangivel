package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar;

import br.com.azi.patrimoniointangivel.domain.entity.DadosAmortizacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.AmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.DadosAmortizacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.converter.DesativarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.exception.DesativarPatrimonioAmortizadoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.exception.DesativarPatrimonioException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class DesativarPatrimonioUseCaseImpl implements DesativarPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private DesativarPatrimonioOutputDataConverter outputDataConverter;

    private DadosAmortizacaoDataProvider dadosAmortizacaoDataProvider;

    private ConfigAmortizacaoDataProvider configAmortizacaoDataProvider;

    private AmortizacaoDataProvider amortizacaoDataProvider;

    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    @Override
    public DesativarPatrimonioOutputData executar(DesativarPatrimonioInputData inputData) {
        validarPatrimonioExiste(inputData);
        Patrimonio patrimonio = buscar(inputData);

        verificarAtivadoNoMesCorrente(patrimonio);
        verificarSeJaAmortizou(patrimonio);
        DadosAmortizacao dadosAmortizacao = patrimonio.getDadosAmortizacao();

        removerLancamentoContabil(patrimonio);
        removerDadosAtivacao(patrimonio);

        Patrimonio patrimonioSalvo = atualizar(patrimonio);

        if (!patrimonio.getVidaIndefinida() && Objects.nonNull(dadosAmortizacao)) {
            removerDadosAmortizacao(dadosAmortizacao);
            removerConfigAmortizacao(dadosAmortizacao);
        }

        return outputDataConverter.to(patrimonioSalvo);
    }

    private void validarPatrimonioExiste(DesativarPatrimonioInputData inputData) {
        if (!patrimonioDataProvider.existe(inputData.getId())) {
            throw new PatrimonioNaoEncontradoException();
        }
    }

    private Patrimonio buscar(DesativarPatrimonioInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getId());
        return patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new);
    }

    private void verificarAtivadoNoMesCorrente(Patrimonio patrimonio) {
        if (!(patrimonio.getDataFinalAtivacao().getMonth().equals(LocalDateTime.now().getMonth())
            && patrimonio.getDataFinalAtivacao().getYear() == LocalDateTime.now().getYear())) {
            throw new DesativarPatrimonioException();
        }
    }

    private void verificarSeJaAmortizou(Patrimonio patrimonio) {
        if(amortizacaoDataProvider.existePorPatrimonio(patrimonio.getId())){
            throw new DesativarPatrimonioAmortizadoException();
        }
    }

    private void removerLancamentoContabil(Patrimonio patrimonio) {
        lancamentosContabeisDataProvider.excluirPorPatrimonio(patrimonio.getId());
    }

    private void removerDadosAtivacao(Patrimonio patrimonio) {
        patrimonio.setInicioVidaUtil(null);
        patrimonio.setFimVidaUtil(null);
        patrimonio.setValorLiquido(null);
        patrimonio.setValorEntrada(null);
        patrimonio.setNumero(null);
        patrimonio.setDataFinalAtivacao(null);
        patrimonio.setDadosAmortizacao(null);
        patrimonio.setSituacao(Patrimonio.Situacao.EM_ELABORACAO);
    }

    private Patrimonio atualizar(Patrimonio patrimonio) {
        return patrimonioDataProvider.atualizar(patrimonio);
    }

    private void removerDadosAmortizacao(DadosAmortizacao dadosAmortizacao) {
        dadosAmortizacaoDataProvider.remover(dadosAmortizacao.getId());
    }

    private void removerConfigAmortizacao(DadosAmortizacao dadosAmortizacao) {
        configAmortizacaoDataProvider.remover(dadosAmortizacao.getConfigAmortizacao().getId());
    }
}
