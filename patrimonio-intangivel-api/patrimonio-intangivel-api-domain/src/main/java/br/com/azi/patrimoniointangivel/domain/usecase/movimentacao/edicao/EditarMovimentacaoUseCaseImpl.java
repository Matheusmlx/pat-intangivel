package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException;
import br.com.azi.patrimoniointangivel.domain.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.conveter.EditarMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.exception.OrgaoDestinoIgualOrgaoOrigemException;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import br.com.azi.patrimoniointangivel.utils.string.StringUtils;
import br.com.azi.patrimoniointangivel.utils.validate.NotaLancamentoContabilValidate;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarMovimentacaoUseCaseImpl implements EditarMovimentacaoUseCase{

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private EditarMovimentacaoOutputDataConverter outputDataConverter;

    @Override
    public EditarMovimentacaoOutputData executar(EditarMovimentacaoInputData inputData) {
     validarEntrada(inputData);
     validarMovimentacaoExiste(inputData);

     Movimentacao movimentacao = buscar(inputData);
     setarDados(inputData, movimentacao);
     Movimentacao movimentacaoAtualizado = atualizar(movimentacao);

     return outputDataConverter.to(movimentacaoAtualizado);
    }

    private void validarEntrada(EditarMovimentacaoInputData inputData){
        Validator.of(inputData)
            .validate(EditarMovimentacaoInputData::getId, Objects::nonNull,"Id é nulo")
            .get();
    }

    private void validarMovimentacaoExiste(EditarMovimentacaoInputData inputData){
        if(!movimentacaoDataProvider.existe(inputData.getId())){
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private Movimentacao buscar(EditarMovimentacaoInputData inputData){
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getId());
        return (movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new));
    }

    private void setarDados(EditarMovimentacaoInputData inputData, Movimentacao movimentacao){

        if(Objects.nonNull(inputData.getMotivo())){
            movimentacao.setMotivo(inputData.getMotivo());
        }

        if(Objects.nonNull(inputData.getOrgaoDestino())){
            movimentacao.setOrgaoDestino(UnidadeOrganizacional.builder().id(inputData.getOrgaoDestino()).build());
            validarSeOrgaoDestinoDiferenteOrgaoOrigem(movimentacao);
        }

        atualizarNotaLancamentoContabil(movimentacao,inputData);
    }

    private void validarSeOrgaoDestinoDiferenteOrgaoOrigem(Movimentacao movimentacao) {
        if(movimentacao.getOrgaoOrigem().getId().equals(movimentacao.getOrgaoDestino().getId())) {
            throw new OrgaoDestinoIgualOrgaoOrigemException();
        }
    }

    private Movimentacao atualizar(Movimentacao movimentacao){
        return movimentacaoDataProvider.salvar(movimentacao);
    }

    private void atualizarNotaLancamentoContabil(Movimentacao movimentacao,EditarMovimentacaoInputData inputData){
        NotaLancamentoContabil notaLancamentoContabil =movimentacao.getNotaLancamentoContabil();

        if(!deveAtualizarNotaLancamentoContabil(notaLancamentoContabil,inputData)){
            return;
        }

        if (Objects.isNull(notaLancamentoContabil)) {
            notaLancamentoContabil = new NotaLancamentoContabil();
        }

        if (StringUtils.isNotEmpty(inputData.getNumeroNL())) {
            validarNumeroNotaLancamentoContabil(notaLancamentoContabil, inputData);
        }

        if (Objects.nonNull(inputData.getDataNL())) {
            NotaLancamentoContabilValidate.validarDataNotaLancamentoContabil(inputData.getDataNL());
            notaLancamentoContabil.setDataLancamento(DateUtils.asLocalDateTime(inputData.getDataNL()));
        } else {
            notaLancamentoContabil.setDataLancamento(null);
        }

        notaLancamentoContabil.setNumero(inputData.getNumeroNL());
        movimentacao.setNotaLancamentoContabil(notaLancamentoContabilDataProvider.salvar(notaLancamentoContabil));
    }

    private boolean deveAtualizarNotaLancamentoContabil(NotaLancamentoContabil notaLancamentoContabil, EditarMovimentacaoInputData inputData){
        return Objects.nonNull(notaLancamentoContabil) || StringUtils.isNotEmpty(inputData.getNumeroNL()) || Objects.nonNull(inputData.getDataNL());
    }

    private void validarNumeroNotaLancamentoContabil(NotaLancamentoContabil notaLancamentoContabil, EditarMovimentacaoInputData inputData) {
        final String numero = inputData.getNumeroNL();

        if (numero.equals(notaLancamentoContabil.getNumero())) {
            return;
        }

        NotaLancamentoContabilValidate.validarFormatoNumeroNotaLancamentoContabil(numero);

        if (notaLancamentoContabilDataProvider.existePorNumero(numero)) {
            throw new IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException();
        }
    }
}
