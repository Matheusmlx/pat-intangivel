package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro;

import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.cadastro.converter.CriarMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.gerarcodigo.GerarCodigoDeMovimentacaoUseCase;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CriarMovimentacaoUseCaseImpl implements CriarMovimentacaoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private CriarMovimentacaoOutputDataConverter outputDataConverter;

    private GerarCodigoDeMovimentacaoUseCase gerarCodigoDeMovimentacaoUseCase;

    @Override
    public CriarMovimentacaoOutputData executar(CriarMovimentacaoInputData inputData) {
        validarEntrada(inputData);
        validarSePatrimonioExiste(inputData);

        Patrimonio patrimonio = buscarPatrimonio(inputData);
        Movimentacao movimentacao = criarMovimentacao(inputData,patrimonio);
        gerarCodigoDeMovimentacao(movimentacao);

        Movimentacao movimentacaoSalva = salvar(movimentacao);

        return outputDataConverter.to(movimentacaoSalva);
    }

    private void validarEntrada(CriarMovimentacaoInputData inputData){
        Validator.of(inputData)
            .validate(CriarMovimentacaoInputData::getTipo, Objects::nonNull,"O Tipo não deve ser nulo")
            .validate(CriarMovimentacaoInputData::getIdPatrimonio,Objects::nonNull,"Sem id do Patrimonio")
            .get();
    }

    private void validarSePatrimonioExiste(CriarMovimentacaoInputData inputData){
        if (!patrimonioDataProvider.existe(inputData.getIdPatrimonio())) {
            throw new PatrimonioNaoEncontradoException();
        }
    }

    private Patrimonio buscarPatrimonio(CriarMovimentacaoInputData inputData){
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getIdPatrimonio());
        return (patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new));
    }

    private Movimentacao criarMovimentacao(CriarMovimentacaoInputData inputData, Patrimonio patrimonio){
        return Movimentacao
            .builder()
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .patrimonio(patrimonio)
            .tipo(Movimentacao.Tipo.valueOf(inputData.getTipo()))
            .orgaoOrigem(patrimonio.getOrgao())
            .build();
    }

    private void gerarCodigoDeMovimentacao(Movimentacao movimentacao){
        GerarCodigoDeMovimentacaoOutputData gerarNumeroMovimentacaoOutputData = gerarCodigoDeMovimentacaoUseCase.executar();
        movimentacao.setCodigo(gerarNumeroMovimentacaoOutputData.getCodigo());
    }

    private Movimentacao salvar(Movimentacao movimentacao){return movimentacaoDataProvider.salvar(movimentacao);}
}
