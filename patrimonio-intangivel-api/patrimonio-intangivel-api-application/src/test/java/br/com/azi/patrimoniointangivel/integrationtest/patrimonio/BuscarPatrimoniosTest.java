package br.com.azi.patrimoniointangivel.integrationtest.patrimonio;

import br.com.azi.patrimoniointangivel.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniointangivel.integrationtest.helper.FileHelper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
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
import org.springframework.transaction.annotation.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/patrimonio/busca-listagem.sql"})
@Transactional
public class BuscarPatrimoniosTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Before
    public void beforeTest() {
        createMockRequisicaoHal();
    }

    @Test
    public void deveBuscarListagemPatrimonio() throws Exception{
        this.mockMvc
                .perform(
                    get("/patrimonios?conteudo=Alura&page=1&size=10&sort=nome&direction=ASC")
                        .headers(AuthenticationHelper.getHeaders())
                        .cookie(AuthenticationHelper.getCookies())
                )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].nome", equalTo("Alura")))
            .andExpect(jsonPath("$.totalElements", equalTo(1)));
    }

    @Test
    public void deveOrdenarListagemPorNome() throws Exception{
        this.mockMvc
            .perform(
                get("/patrimonios?page=1&size=10&sort=nome&direction=ASC")
                    .headers(AuthenticationHelper.getHeaders())
                    .cookie(AuthenticationHelper.getCookies())
            )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].nome", equalTo("Alura")));
    }

    private void createMockRequisicaoHal() {
        String response = FileHelper.getJson("setup", "unidade-organizacionais.json");

        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/unidadeOrganizacionalDominio/1/buscarOrgaosPorFuncao?funcao=Patrimonio.Normal&funcao=Patrimonio.Consulta"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }
}
