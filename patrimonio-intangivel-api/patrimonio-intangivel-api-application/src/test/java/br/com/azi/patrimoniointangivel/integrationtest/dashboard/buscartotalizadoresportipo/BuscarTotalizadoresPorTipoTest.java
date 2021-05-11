package br.com.azi.patrimoniointangivel.integrationtest.dashboard.buscartotalizadoresportipo;

import br.com.azi.patrimoniointangivel.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniointangivel.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniointangivel.integrationtest.helper.JsonHelper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarTotalizadoresPorTipoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-totalizadores-por-tipo.sql"})
    @Transactional
    public void buscarComUsuarioNaoVinculadoANenhumOrgao() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of();

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/totalizadores-por-tipo")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.itens", hasSize(6)))
            .andExpect(jsonPath("$.itens[0].nome", is("Softwares")))
            .andExpect(jsonPath("$.itens[0].tipo", is("SOFTWARES")))
            .andExpect(jsonPath("$.itens[0].quantidade", is(0)))
            .andExpect(jsonPath("$.itens[1].nome", is("Direitos Autorais")))
            .andExpect(jsonPath("$.itens[1].tipo", is("DIREITOS_AUTORAIS")))
            .andExpect(jsonPath("$.itens[1].quantidade", is(0)))
            .andExpect(jsonPath("$.itens[2].nome", is("Licenças")))
            .andExpect(jsonPath("$.itens[2].tipo", is("LICENCAS")))
            .andExpect(jsonPath("$.itens[2].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[3].nome", is("Marcas")))
            .andExpect(jsonPath("$.itens[3].tipo", is("MARCAS")))
            .andExpect(jsonPath("$.itens[3].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[4].nome", is("Títulos de Publicação")))
            .andExpect(jsonPath("$.itens[4].tipo", is("TITULOS_DE_PUBLICACAO")))
            .andExpect(jsonPath("$.itens[4].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[5].nome", is("Receitas, Fórmulas e Projetos")))
            .andExpect(jsonPath("$.itens[5].tipo", is("RECEITAS_FORMULAS_PROJETOS")))
            .andExpect(jsonPath("$.itens[5].quantidade", is(1)));

    }

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-totalizadores-por-tipo.sql"})
    @Transactional
    public void buscarComUsuarioVinculadoAUmOrgao() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of(
            UnidadeOrganizacional
                .builder()
                .id(100L)
                .build()
        );

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/totalizadores-por-tipo")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.itens", hasSize(6)))
            .andExpect(jsonPath("$.itens[0].nome", is("Softwares")))
            .andExpect(jsonPath("$.itens[0].tipo", is("SOFTWARES")))
            .andExpect(jsonPath("$.itens[0].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[1].nome", is("Direitos Autorais")))
            .andExpect(jsonPath("$.itens[1].tipo", is("DIREITOS_AUTORAIS")))
            .andExpect(jsonPath("$.itens[1].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[2].nome", is("Licenças")))
            .andExpect(jsonPath("$.itens[2].tipo", is("LICENCAS")))
            .andExpect(jsonPath("$.itens[2].quantidade", is(2)))
            .andExpect(jsonPath("$.itens[3].nome", is("Marcas")))
            .andExpect(jsonPath("$.itens[3].tipo", is("MARCAS")))
            .andExpect(jsonPath("$.itens[3].quantidade", is(2)))
            .andExpect(jsonPath("$.itens[4].nome", is("Títulos de Publicação")))
            .andExpect(jsonPath("$.itens[4].tipo", is("TITULOS_DE_PUBLICACAO")))
            .andExpect(jsonPath("$.itens[4].quantidade", is(2)))
            .andExpect(jsonPath("$.itens[5].nome", is("Receitas, Fórmulas e Projetos")))
            .andExpect(jsonPath("$.itens[5].tipo", is("RECEITAS_FORMULAS_PROJETOS")))
            .andExpect(jsonPath("$.itens[5].quantidade", is(2)));
    }

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-totalizadores-por-tipo.sql"})
    @Transactional
    public void buscarComUsuarioVinculadoATodosOsOrgao() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of(
            UnidadeOrganizacional
                .builder()
                .id(100L)
                .build(),
            UnidadeOrganizacional
                .builder()
                .id(200L)
                .build()
        );

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/totalizadores-por-tipo")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.itens", hasSize(6)))
            .andExpect(jsonPath("$.itens[0].nome", is("Softwares")))
            .andExpect(jsonPath("$.itens[0].tipo", is("SOFTWARES")))
            .andExpect(jsonPath("$.itens[0].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[1].nome", is("Direitos Autorais")))
            .andExpect(jsonPath("$.itens[1].tipo", is("DIREITOS_AUTORAIS")))
            .andExpect(jsonPath("$.itens[1].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[2].nome", is("Licenças")))
            .andExpect(jsonPath("$.itens[2].tipo", is("LICENCAS")))
            .andExpect(jsonPath("$.itens[2].quantidade", is(2)))
            .andExpect(jsonPath("$.itens[3].nome", is("Marcas")))
            .andExpect(jsonPath("$.itens[3].tipo", is("MARCAS")))
            .andExpect(jsonPath("$.itens[3].quantidade", is(2)))
            .andExpect(jsonPath("$.itens[4].nome", is("Títulos de Publicação")))
            .andExpect(jsonPath("$.itens[4].tipo", is("TITULOS_DE_PUBLICACAO")))
            .andExpect(jsonPath("$.itens[4].quantidade", is(3)))
            .andExpect(jsonPath("$.itens[5].nome", is("Receitas, Fórmulas e Projetos")))
            .andExpect(jsonPath("$.itens[5].tipo", is("RECEITAS_FORMULAS_PROJETOS")))
            .andExpect(jsonPath("$.itens[5].quantidade", is(3)));
    }

    private void createMockRequisicaoHal(List responseHal) {
        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/unidadeOrganizacionalDominio/1/buscarOrgaosPorFuncao?funcao=Patrimonio.Normal&funcao=Patrimonio.Consulta"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(JsonHelper.toJson(responseHal))));
    }
}
