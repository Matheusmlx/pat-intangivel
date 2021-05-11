package br.com.azi.patrimoniointangivel.integrationtest.movimentacao.buscarmovimentacoes;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@Sql({"/datasets/movimentacao/buscar-movimentacao.sql"})
public class BuscarMovimentacoesTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Before
    public void beforeTest() {
        createMockRequisicaoHal();
    }

    @Test
    public void deveBuscarTodasAsMovimentacoesPorFiltro() throws Exception{
        this.mockMvc.perform(
            get("/movimentacoes?page=1&size=10&sort=situacao&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.jsonPath("$.items.size()", equalTo(9)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id",equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].codigo",equalTo("0003")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].tipo",equalTo("DOACAO_ENTRE_ORGAOS")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].id",equalTo(6)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].codigo",equalTo("0006")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].tipo",equalTo("DOACAO_ENTRE_ORGAOS")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].codigo",equalTo("0001")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].tipo",equalTo("DOACAO_ENTRE_ORGAOS")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[3].id",equalTo(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[3].codigo",equalTo("0002")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[3].tipo",equalTo("DOACAO_ENTRE_ORGAOS")));

    }

    @Test
    public void deveBuscarTodasAsMovimentacaoEmElaboracao() throws Exception{
        this.mockMvc.perform(
            get("/movimentacoes?conteudo=EM_ELABORACAO&page=1&size=10&sort=situacao&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.jsonPath("$.items.size()", equalTo(1)));
    }

    @Test
    public void deveBuscarTodasAsMovimetacoesPendentes() throws Exception{
        this.mockMvc.perform(
            get("/movimentacoes?conteudo=AGUARDANDO_RECEBIMENTO&page=1&size=10&sort=situacao&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.jsonPath("$.items.size()", equalTo(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id",equalTo(6)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].codigo",equalTo("0006")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].tipo",equalTo("DOACAO_ENTRE_ORGAOS")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].id",equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].codigo",equalTo("0001")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].tipo",equalTo("DOACAO_ENTRE_ORGAOS")));
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
