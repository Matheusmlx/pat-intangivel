package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro;

import br.com.azi.patrimoniointangivel.domain.entity.CodigoContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.exception.ContaContabilNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.converter.CadastraPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.exception.EscolheTipoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoOutputData;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.gerarnumero.memorando.GerarNumeroMemorandoUseCase;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CadastraPatrimonioUseCaseImpl implements CadastraPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private CadastraPatrimonioOutputDataConverter outputDataConverter;

    private ContaContabilDataProvider contaContabilDataProvider;

    private GerarNumeroMemorandoUseCase gerarNumeroMemorandoUseCase;

    private CodigoContaContabil codigoContaContabil;

    @Override
    public CadastraPatrimonioOutputData executar(CadastraPatrimonioInputData input) {
        validarEntrada(input);
        validarTipo(input);

        Patrimonio patrimonio = criarEntidade(input);
        gerarNumeroMemorando(patrimonio);
        setarContaContabil(patrimonio);
        Patrimonio patrimonioSalvo = salvar(patrimonio);

        return outputDataConverter.to(patrimonioSalvo);
    }

    private void validarEntrada(CadastraPatrimonioInputData inputData) {
        Validator.of(inputData)
            .validate(CadastraPatrimonioInputData::getTipo, Objects::nonNull, "O tipo é nulo")
            .get();
    }

    private void validarTipo(CadastraPatrimonioInputData inputData) {
        if (!ObjectUtils.containsConstant(Patrimonio.Tipo.values(), inputData.getTipo(), true)) {
            throw new EscolheTipoException();
        }
    }

    private Patrimonio criarEntidade(CadastraPatrimonioInputData input) {
        return Patrimonio
            .builder()
            .tipo(Patrimonio.Tipo.valueOf(input.getTipo()))
            .situacao(Patrimonio.Situacao.EM_ELABORACAO)
            .build();
    }

    private void gerarNumeroMemorando(Patrimonio patrimonio) {
        GerarNumeroMemorandoOutputData gerarNumeroMemorandoOutputData = gerarNumeroMemorandoUseCase.executar();
        patrimonio.setNumeroMemorando(gerarNumeroMemorandoOutputData.getNumeroMemorando());
        patrimonio.setAnoMemorando(Calendar.getInstance().get(Calendar.YEAR));
    }

    private void setarContaContabil(Patrimonio patrimonio) {
        Optional<ContaContabil> contaContabil;
        switch (patrimonio.getTipo()) {
            case SOFTWARES:
                patrimonio.setContaContabil(null);
                break;
            case LICENCAS:
            case TITULOS_DE_PUBLICACAO:
            case RECEITAS_FORMULAS_PROJETOS:
                contaContabil = contaContabilDataProvider.buscarPorCodigo(codigoContaContabil.getOutrosDireitosBensIntangiveis());
                patrimonio.setContaContabil(contaContabil.orElseThrow(ContaContabilNaoEncontradaException::new));
                break;
            case MARCAS:
                contaContabil = contaContabilDataProvider.buscarPorCodigo(codigoContaContabil.getMarcasPatentesIndustriais());
                patrimonio.setContaContabil(contaContabil.orElseThrow(ContaContabilNaoEncontradaException::new));
                break;
            case DIREITOS_AUTORAIS:
                contaContabil = contaContabilDataProvider.buscarPorCodigo(codigoContaContabil.getDireitosAutorais());
                patrimonio.setContaContabil(contaContabil.orElseThrow(ContaContabilNaoEncontradaException::new));
                break;
        }
    }

    private Patrimonio salvar(Patrimonio patrimonio) {
        return patrimonioDataProvider.salvar(patrimonio);
    }
}
