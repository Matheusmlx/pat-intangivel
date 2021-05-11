package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber;

import br.com.azi.patrimoniointangivel.domain.entity.LancamentosContabeis;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.LancamentosContabeisDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UnidadeOrganizacionalDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.exception.SituacaoEmElaboracaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.receber.exception.SituacaoOrgaoDestinoInativoException;
import br.com.azi.patrimoniointangivel.domain.usecase.relatorio.inventario.sintetico.exception.OrgaoNaoEncontradoException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class ReceberMovimentacaoUseCaseImpl implements ReceberMovimentacaoUseCase{

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private PatrimonioDataProvider patrimonioDataProvider;

    private UnidadeOrganizacionalDataProvider unidadeOrganizacionalDataProvider;

    private UsuarioDataProvider usuarioDataProvider;

    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private LancamentosContabeisDataProvider lancamentosContabeisDataProvider;

    private Clock clock;

    @Override
    public void executar(ReceberMovimentacaoInputData inputData) {
        validarEntrada(inputData);

        Movimentacao movimentacao = buscarMovimentacao(inputData);
        validarSeEstadoDiferenteDeEmElaboracao(movimentacao);

        UnidadeOrganizacional orgaoEncontrado =  buscarOrgao(movimentacao);
        validarSituacaoDoOrgao(orgaoEncontrado);

        Patrimonio patrimonio = buscarPatrimonio(movimentacao.getPatrimonio().getId());

        setarPatrimonio(patrimonio, movimentacao);
        atualizarPatrimonio(patrimonio);

        setarMovimentacao(movimentacao);

        LancamentosContabeis lancamentoContabilNoOrgaoOrigem = gerarLancamentoContabilDebito(patrimonio, movimentacao);
        salvarLancamentoContabil(lancamentoContabilNoOrgaoOrigem);

        LancamentosContabeis lancamentoContabilNoOrgaoDestino = gerarLancamentoContabilCredito(patrimonio, movimentacao);
        salvarLancamentoContabil(lancamentoContabilNoOrgaoDestino);

        atualizar(movimentacao);
    }

    private void validarEntrada(ReceberMovimentacaoInputData inputData){
        Validator.of(inputData)
            .validate(ReceberMovimentacaoInputData::getId, Objects::nonNull,"Id da movimentação é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(ReceberMovimentacaoInputData inputData){
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarSeEstadoDiferenteDeEmElaboracao(Movimentacao movimentacao){
        if(movimentacao.getSituacao().equals(Movimentacao.Situacao.EM_ELABORACAO)){
            throw new SituacaoEmElaboracaoException();
        }
    }

    private UnidadeOrganizacional buscarOrgao(Movimentacao movimentacao){
        Optional<UnidadeOrganizacional> orgao = unidadeOrganizacionalDataProvider.buscarPorId(movimentacao.getOrgaoDestino().getId());
       return  orgao.orElseThrow(OrgaoNaoEncontradoException::new);
    }

    private void validarSituacaoDoOrgao(UnidadeOrganizacional orgao){
        if(orgao.getSituacao().equals("INATIVO")){
            throw new  SituacaoOrgaoDestinoInativoException();
        }
    }

    private Patrimonio buscarPatrimonio(Long id){
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(id);
        return (patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new));
    }

    private void setarPatrimonio(Patrimonio patrimonio, Movimentacao movimentacao){
        patrimonio.setOrgao(movimentacao.getOrgaoDestino());
        patrimonio.setSetor(null);
        patrimonio.setValorEntrada(patrimonio.getValorLiquido());
    }

    private void atualizarPatrimonio(Patrimonio patrimonio){patrimonioDataProvider.atualizar(patrimonio);}

    private LancamentosContabeis gerarLancamentoContabilDebito(Patrimonio patrimonio, Movimentacao movimentacao) {
        return LancamentosContabeis
            .builder()
            .patrimonio(patrimonio)
            .movimentacao(movimentacao)
            .orgao(movimentacao.getOrgaoOrigem())
            .contaContabil(patrimonio.getContaContabil())
            .tipoLancamento(LancamentosContabeis.TipoLancamento.DEBITO)
            .motivoLancamento(buscarTipoLancamento(movimentacao))
            .valorLiquido(patrimonio.getValorLiquido())
            .dataLancamento(LocalDateTime.now())
            .build();
    }

    private LancamentosContabeis gerarLancamentoContabilCredito(Patrimonio patrimonio, Movimentacao movimentacao) {
        return LancamentosContabeis
            .builder()
            .patrimonio(patrimonio)
            .movimentacao(movimentacao)
            .orgao(movimentacao.getOrgaoDestino())
            .contaContabil(patrimonio.getContaContabil())
            .tipoLancamento(LancamentosContabeis.TipoLancamento.CREDITO)
            .motivoLancamento(buscarTipoLancamento(movimentacao))
            .valorLiquido(patrimonio.getValorLiquido())
            .dataLancamento(LocalDateTime.now())
            .build();
    }

    private LancamentosContabeis.MotivoLancamento buscarTipoLancamento(Movimentacao movimentacao) {
        if (movimentacao.getTipo().equals(Movimentacao.Tipo.TRANSFERENCIA_DEFINITIVA)) {
            return LancamentosContabeis.MotivoLancamento.TRANSFERENCIA_DEFINITIVA;
        }
        return LancamentosContabeis.MotivoLancamento.DOACAO_ENTRE_ORGAOS;
    }

    private void salvarLancamentoContabil(LancamentosContabeis lancamentosContabeis) {
        lancamentosContabeisDataProvider.salvar(lancamentosContabeis);
    }

    private void setarMovimentacao(Movimentacao movimentacao){
        movimentacao.setSituacao(Movimentacao.Situacao.FINALIZADO);
        movimentacao.setDataDeFinalizacao(LocalDateTime.now(clock));
        movimentacao.setUsuarioFinalizacao(usuarioDataProvider.buscarUsuarioPorSessao(sessaoUsuarioDataProvider.get()).getNome());
    }

    private void atualizar(Movimentacao movimentacao){ movimentacaoDataProvider.salvar(movimentacao); }
}
