package br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem;

import br.com.azi.patrimoniointangivel.domain.constant.permissoes.PermissaoPatrimonioConstants;
import br.com.azi.patrimoniointangivel.domain.entity.ListaPaginada;
import br.com.azi.patrimoniointangivel.domain.entity.Movimentacao;
import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniointangivel.domain.usecase.Validator;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.converter.BuscarMovimentacaoFiltroConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.buscarlistagem.converter.BuscarMovimentacoesOutputDataConverter;
import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.buscarlistagem.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarMovimentacoesUseCaseImpl implements BuscarMovimentacoesUseCase{

    private BuscarMovimentacaoFiltroConverter buscarMovimentacaoFiltroConverter;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private MovimentacaoDataProvider movimentacaoDataProvider;

    private BuscarMovimentacoesOutputDataConverter outputDataConverter;

    @Override
    public BuscarMovimentacoesOutputData executar(BuscarMovimentacoesInputData inputData) {
        validarDadosEntrada(inputData);

        Movimentacao.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<Movimentacao> entidadesEncontradas = buscar(filtro);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarMovimentacoesInputData inputData){
        Validator.of(inputData)
            .validate(BuscarMovimentacoesInputData::getSize,size-> Objects.nonNull(size) && size >0,new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarMovimentacoesInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private Movimentacao.Filtro criarFiltro(BuscarMovimentacoesInputData inputData){
        Movimentacao.Filtro filtro = buscarMovimentacaoFiltroConverter.to(inputData);
        filtro.setUnidadeOrganizacionalIds(buscarOrgaosIdsAcessoUsuario());
        return filtro;
    }

    private List<Long> buscarOrgaosIdsAcessoUsuario() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoPatrimonioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoPatrimonioConstants.CONSULTA.getDescription());

        List<UnidadeOrganizacional> unidadeOrganizacionals = sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);

        return unidadeOrganizacionals
            .stream()
            .map(UnidadeOrganizacional::getId)
            .collect(Collectors.toList());
    }

    private ListaPaginada<Movimentacao> buscar(Movimentacao.Filtro filtro){
        ListaPaginada<Movimentacao> movimentacoes = movimentacaoDataProvider.buscarPorFiltro(filtro);

        return movimentacoes;
    }
}
