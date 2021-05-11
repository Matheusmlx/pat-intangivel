package br.com.azi.patrimoniointangivel.domain.usecase.fornecedor.buscarlistagem;

import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.exception.FiltroIncompletoException;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.fornecedor.buscarlistagem.converter.BuscarFornecedoresFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.fornecedor.buscarlistagem.converter.BuscarFornecedoresOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarFornecedoresUseCaseImpl implements BuscarFornecedoresUseCase {

    private SistemaDeFornecedoresIntegration sistemaDeFornecedoresIntegration;

    private BuscarFornecedoresFiltroConverter filtroConverter;

    private BuscarFornecedoresOutputDataConverter outputDataConverter;

    @Override
    public BuscarFornecedoresOutputData executar(BuscarFornecedoresInputData inputData) {
        validarDadosEntrada(inputData);

        Fornecedor.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<Fornecedor> entidadesEncontradas = buscar(filtro);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarFornecedoresInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarFornecedoresInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarFornecedoresInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .validate(BuscarFornecedoresInputData::getConteudo, Objects::nonNull, new FiltroIncompletoException("Ausência do conteudo."))
            .get();
    }

    private Fornecedor.Filtro criarFiltro(BuscarFornecedoresInputData inputData) {
        return this.filtroConverter.to(inputData);
    }

    private ListaPaginada<Fornecedor> buscar(Fornecedor.Filtro filtro) {
        return sistemaDeFornecedoresIntegration.buscarPorFiltro(filtro);
    }
}
