package br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao;

import br.com.azi.patrimoniointangivel.domain.entity.CodigoContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniointangivel.domain.entity.Patrimonio;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.exception.ContaContabilNaoEncontradaException;
import br.com.azi.patrimoniointangivel.domain.exception.IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException;
import br.com.azi.patrimoniointangivel.domain.exception.PatrimonioNaoEncontradoException;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.converter.EditarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception.DataAtivacaoMenorQueDataAquisicaoException;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.exception.PatrimonioNomeInvalidoException;
import br.com.azi.patrimoniointangivel.utils.date.DateUtils;
import br.com.azi.patrimoniointangivel.utils.string.StringUtils;
import br.com.azi.patrimoniointangivel.utils.validate.NotaLancamentoContabilValidate;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarPatrimonioUseCaseImpl implements EditarPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private EditarPatrimonioOutputDataConverter outputDataConverter;

    private ContaContabilDataProvider contaContabilDataProvider;

    private CodigoContaContabil codigoContaContabil;

    private NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Override
    public EditarPatrimonioOutputData executar(EditarPatrimonioInputData inputData) {

        validarDadosEntrada(inputData);
        validarNomePatrimonio(inputData);
        validarPatrimonioExiste(inputData);

        Patrimonio patrimonio = buscar(inputData);
        setarDados(patrimonio, inputData);

        setaContaContabilSoftware(patrimonio);
        Patrimonio patrimonioSalvo = atualizar(patrimonio);

        return outputDataConverter.to(patrimonioSalvo);
    }

    private void validarDadosEntrada(EditarPatrimonioInputData inputData) {
        Validator.of(inputData)
            .validate(EditarPatrimonioInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validaSeDataDeAtivacaoMenorQueDataDeAquisicao(EditarPatrimonioInputData inputData) {
        if (Objects.nonNull(inputData.getDataAquisicao()) && Objects.nonNull(inputData.getDataAtivacao())
            && inputData.getDataAtivacao().before(inputData.getDataAquisicao())) {
            throw new DataAtivacaoMenorQueDataAquisicaoException();
        }
    }

    private void validarNomePatrimonio(EditarPatrimonioInputData inputData) {
        if (Objects.nonNull(inputData.getNome()) && inputData.getNome().length() > 100) {
                throw new PatrimonioNomeInvalidoException();
        }
    }

    private void validarPatrimonioExiste(EditarPatrimonioInputData inputData) {
        if (!patrimonioDataProvider.existe(inputData.getId())) {
            throw new PatrimonioNaoEncontradoException();
        }
    }

    private Patrimonio buscar(EditarPatrimonioInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPorId(inputData.getId());
        return (patrimonio.orElseThrow(PatrimonioNaoEncontradoException::new));
    }

    private Boolean verificaAlteracaoTipo(Patrimonio patrimonio, EditarPatrimonioInputData inputData) {
        return !patrimonio.getTipo().name().equals(inputData.getTipo());
    }

    private void atualizaContaContabil(Patrimonio patrimonio) {
        Optional<ContaContabil> contaContabil;
        switch (patrimonio.getTipo()) {
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
            default:
                break;
        }
    }

    private void setarDados(Patrimonio patrimonio, EditarPatrimonioInputData inputData) {

        patrimonio.setDescricao(inputData.getDescricao());
        patrimonio.setVidaIndefinida(inputData.getVidaIndefinida());

        if (Objects.nonNull(inputData.getSituacao())) {
            patrimonio.setSituacao(Patrimonio.Situacao.valueOf(inputData.getSituacao()));
        }

        if (Objects.nonNull(inputData.getReconhecimento())) {
            patrimonio.setReconhecimento(Patrimonio.Reconhecimento.valueOf(inputData.getReconhecimento()));
        }

        if (Objects.nonNull(inputData.getDataAquisicao())) {
                patrimonio.setDataAquisicao(LocalDateTime.ofInstant(inputData.getDataAquisicao().toInstant(),
                    ZoneId.systemDefault()).toLocalDate().atStartOfDay());
        } else {
                patrimonio.setDataAquisicao(null);
        }

        if (Objects.nonNull(inputData.getDataVencimento())) {
                patrimonio.setDataVencimento(LocalDateTime.ofInstant(inputData.getDataVencimento().toInstant(),
                    ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59));
        } else {
                patrimonio.setDataVencimento(null);
        }

        if (Objects.nonNull(inputData.getTipo()) && verificaAlteracaoTipo(patrimonio, inputData)) {
            patrimonio.setTipo(Patrimonio.Tipo.valueOf(inputData.getTipo()));
            atualizaContaContabil(patrimonio);
        }

        if (Objects.nonNull(inputData.getEstado())) {
            patrimonio.setEstado(Patrimonio.Estado.valueOf(inputData.getEstado()));
        }

       if (Objects.nonNull(inputData.getValorAquisicao())) {
            if (verificaSeValorAquisicaoAlterado(patrimonio, inputData)) {
                patrimonio.setValorAquisicao(inputData.getValorAquisicao());
            }
        } else {
           if (verificaSeValorAquisicaoAlterado(patrimonio, inputData)) {
               patrimonio.setValorAquisicao(null);
           }
        }

        if (Objects.nonNull(inputData.getNome())) {
                patrimonio.setNome(inputData.getNome().trim());
        } else {
            patrimonio.setNome(null);
        }

        if (Objects.nonNull(inputData.getAtivacaoRetroativa())) {
            patrimonio.setAtivacaoRetroativa(inputData.getAtivacaoRetroativa());
        }

        if (Objects.nonNull(inputData.getOrgao())) {
            patrimonio.setOrgao(UnidadeOrganizacional.builder()
                .id(inputData.getOrgao())
                .build());
        }

        if (Objects.nonNull(inputData.getSetor())) {
            patrimonio.setSetor(UnidadeOrganizacional.builder()
            .id(inputData.getSetor())
            .build());
        } else {
            patrimonio.setSetor(null);
        }

        if (Objects.nonNull(inputData.getFornecedor())) {
            patrimonio.setFornecedor(Fornecedor
                .builder()
                .id(inputData.getFornecedor())
                .build());
        } else {
            patrimonio.setFornecedor(null);
        }

        if (Objects.nonNull(inputData.getDataAtivacao())) {
            validaSeDataDeAtivacaoMenorQueDataDeAquisicao(inputData);
            patrimonio.setDataAtivacao(LocalDateTime.ofInstant(inputData.getDataAtivacao().toInstant(),
                    ZoneId.systemDefault()).toLocalDate().atStartOfDay());

        } else {
                patrimonio.setDataAtivacao(null);
        }
        atualizarNotaLancamentoContabil(patrimonio,inputData);
    }

    private void setaContaContabilSoftware(Patrimonio patrimonio) {
        Optional<ContaContabil> contaContabil;
        if (patrimonio.getTipo().equals(Patrimonio.Tipo.SOFTWARES)) {
            if (Objects.nonNull(patrimonio.getEstado())) {
                if (patrimonio.getEstado().equals(Patrimonio.Estado.EM_DESENVOLVIMENTO)) {
                    contaContabil = contaContabilDataProvider.buscarPorCodigo(codigoContaContabil.getSoftwareDesenvolvimento());
                } else {
                    contaContabil = contaContabilDataProvider.buscarPorCodigo(codigoContaContabil.getBensIntangiveisSoftware());
                }
                patrimonio.setContaContabil(contaContabil.orElseThrow(ContaContabilNaoEncontradaException::new));
            } else {
                patrimonio.setContaContabil(null);
            }
        }
    }

    private Patrimonio atualizar(Patrimonio patrimonio) {
        return patrimonioDataProvider.atualizar(patrimonio);
    }

    private Boolean verificaSeValorAquisicaoAlterado(Patrimonio patrimonio, EditarPatrimonioInputData inputData) {
        if (Objects.nonNull(patrimonio.getValorAquisicao())) {
            return  !patrimonio.getValorAquisicao().equals(inputData.getValorAquisicao());
        }
        return Objects.nonNull(inputData.getValorAquisicao());
    }

    private void atualizarNotaLancamentoContabil(Patrimonio patrimonio, EditarPatrimonioInputData inputData){
        NotaLancamentoContabil notaLancamentoContabil = patrimonio.getNotaLancamentoContabil();

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
        }

        notaLancamentoContabil.setNumero(inputData.getNumeroNL());
        notaLancamentoContabil.setDataLancamento(DateUtils.asLocalDateTime(inputData.getDataNL()));
        patrimonio.setNotaLancamentoContabil(notaLancamentoContabilDataProvider.salvar(notaLancamentoContabil));
    }

    private boolean deveAtualizarNotaLancamentoContabil(NotaLancamentoContabil notaLancamentoContabil, EditarPatrimonioInputData inputData){
        return Objects.nonNull(notaLancamentoContabil) || StringUtils.isNotEmpty(inputData.getNumeroNL()) || Objects.nonNull(inputData.getDataNL());
    }

    private void validarNumeroNotaLancamentoContabil(NotaLancamentoContabil notaLancamentoContabil, EditarPatrimonioInputData inputData) {
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
