package br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar;

import br.com.azi.patrimoniointangivel.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ContaContabil;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.ContaContabilDataProvider;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.converter.BuscarContasContabeisFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.converter.BuscarContasContabeisOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscar.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarContasContabeisUseCaseImpl implements BuscarContasContabeisUseCase {

    private ContaContabilDataProvider contaContabilDataProvider;

    private ConfigContaContabilDataProvider configContaContabilDataProvider;

    private Long produtoId;

    private BuscarContasContabeisFiltroConverter filtroConverter;

    private BuscarContasContabeisOutputDataConverter outputDataConverter;

    @Override
    public BuscarContasContabeisOutputData executar(BuscarContasContabeisInputData inputData) {
        validarDadosEntrada(inputData);

        ContaContabil.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<ContaContabil> entidadesEncontradas = buscar(filtro);
        carregarTipos(entidadesEncontradas);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarContasContabeisInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarContasContabeisInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarContasContabeisInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private ContaContabil.Filtro criarFiltro(BuscarContasContabeisInputData inputData) {
        ContaContabil.Filtro filtro = filtroConverter.to(inputData);
        filtro.setProdutoId(produtoId);
        return filtro;
    }

    private ListaPaginada<ContaContabil> buscar(ContaContabil.Filtro filtro) {
        return contaContabilDataProvider.buscarPorFiltro(filtro);
    }

    private void carregarTipos(ListaPaginada<ContaContabil> entidades) {
        entidades
            .getItems()
            .forEach(this::carregarTipo);
    }

    private void carregarTipo(ContaContabil entidade) {
        Optional<ConfigContaContabil> configContaContabil = configContaContabilDataProvider.buscarAtualPorContaContabil(entidade.getId());
        configContaContabil.ifPresent(depreciacao -> entidade.setConfigContaContabil(configContaContabil.get()));
    }
}
