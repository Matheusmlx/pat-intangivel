package br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporid;

import br.com.azi.patrimoniointangivel.domain.entity.Fornecedor;
import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.exception.BuscarFornecedorException;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.exception.BuscarUnidadesOrganizacionaisException;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.entity.EfornecedorIntegrationProperties;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporid.converter.BuscarFornecedorPorIdIntegrationConverter;
import br.com.azi.patrimoniointangivel.gateway.integration.efornecedor.usecase.buscarporid.entity.BuscarFornecedorPorIdIntegrationResponse;
import br.com.azi.patrimoniointangivel.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniointangivel.gateway.integration.utils.JsonUtils;
import br.com.azi.patrimoniointangivel.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniointangivel.gateway.integration.utils.UrlBuilder;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BuscarFornecedorPorIdIntegrationUseCase {

    private static final String PATH_TO_BUSCA = "/fornecedores/portal/%s/resumo";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private BuscarFornecedorPorIdIntegrationConverter integrationConverter;

    public Fornecedor executar(Long id, SessaoUsuario sessaoUsuario, EfornecedorIntegrationProperties integrationProperties) {
        String url = criarUrl(id, integrationProperties);
        Request request = criarRequisicao(url, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            BuscarFornecedorPorIdIntegrationResponse responseEntity = converterRetorno(response);

            return integrationConverter.to(responseEntity);
        } catch (IOException e) {
            throw new BuscarUnidadesOrganizacionaisException("N??o foi possivel buscarorgao listagem de estruturas organizacionais no setup.", e);
        }
    }

    private String criarUrl(Long id, EfornecedorIntegrationProperties integrationProperties) {
        return urlBuilder
            .domain(integrationProperties.getUrl())
            .path(PATH_TO_BUSCA)
            .params(id.toString())
            .build();
    }

    private Request criarRequisicao(String url, SessaoUsuario sessaoUsuario) {
        return new Request.Builder()
            .get()
            .addHeader("Cookie", CookieUtils.getCookieAutenticacao(sessaoUsuario))
            .url(url)
            .build();
    }

    private void handleStatusError(Response response) {
        if (!response.isSuccessful()) {
            throw new BuscarFornecedorException("N??o foi possivel realizar fornecedor no efornecedor");
        }
    }

    private BuscarFornecedorPorIdIntegrationResponse converterRetorno(Response response) throws IOException {
        return jsonUtils.fromJson(response.body().string(), BuscarFornecedorPorIdIntegrationResponse.class);
    }
}
