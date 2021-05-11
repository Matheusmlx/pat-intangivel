package br.com.azi.patrimoniointangivel.integrationtest.dashboard.buscarmetricasdospatrimoniospororgao;

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
public class BuscarMetricasDosPatrimoniosPorOrgaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-metricas-dos-patrimonios-por-orgao.sql"})
    @Transactional
    public void BuscarMetricasDosPatrimoniosPorOrgaoComUsuarioQueNaoEstaVinculadoANenhumOrgao() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of();

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/metricas-por-orgao")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.itens", hasSize(0)));
    }

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-metricas-dos-patrimonios-por-orgao.sql"})
    @Transactional
    public void OrgaoDoUsuarioSemVinculoComPatrimonio() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of(
            UnidadeOrganizacional
                .builder()
                .id(250L)
                .nome("Nome A")
                .sigla("Sigla A")
                .build()
        );

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/metricas-por-orgao")
                .param("quantidade", "5")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.itens", hasSize(0)));
    }

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-metricas-dos-patrimonios-por-orgao.sql"})
    @Transactional
    public void UsuarioVinculadoADoisOrgao() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of(
            UnidadeOrganizacional
                .builder()
                .id(200L)
                .nome("SAD")
                .sigla("sigla200")
                .build(),
            UnidadeOrganizacional
                .builder()
                .id(100L)
                .nome("AGEPEN")
                .sigla("sigla100")
                .build()
        );

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/metricas-por-orgao")
                .param("quantidade", "5")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.itens", hasSize(2)))
            .andExpect(jsonPath("$.itens[0].idOrgao", is(100)))
            .andExpect(jsonPath("$.itens[0].sigla", is("sigla100")))
            .andExpect(jsonPath("$.itens[0].nome", is("AGEPEN")))
            .andExpect(jsonPath("$.itens[0].total", is(2)))
            .andExpect(jsonPath("$.itens[0].tipos", hasSize(2)))
            .andExpect(jsonPath("$.itens[0].tipos[0].nome", is("DIREITOS_AUTORAIS")))
            .andExpect(jsonPath("$.itens[0].tipos[0].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[0].tipos[1].nome", is("SOFTWARES")))
            .andExpect(jsonPath("$.itens[0].tipos[1].quantidade", is(1)))

            .andExpect(jsonPath("$.itens[1].idOrgao", is(200)))
            .andExpect(jsonPath("$.itens[1].sigla", is("sigla200")))
            .andExpect(jsonPath("$.itens[1].nome", is("SAD")))
            .andExpect(jsonPath("$.itens[1].tipos", hasSize(2)))
            .andExpect(jsonPath("$.itens[1].total", is(3)))
            .andExpect(jsonPath("$.itens[1].tipos[0].nome", is("DIREITOS_AUTORAIS")))
            .andExpect(jsonPath("$.itens[1].tipos[0].quantidade", is(1)))
            .andExpect(jsonPath("$.itens[1].tipos[1].nome", is("SOFTWARES")))
            .andExpect(jsonPath("$.itens[1].tipos[1].quantidade", is(2)));
    }

    private void createMockRequisicaoHal(List<UnidadeOrganizacional> responseHal) {
        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/unidadeOrganizacionalDominio/1/buscarOrgaosPorFuncao?funcao=Patrimonio.Normal&funcao=Patrimonio.Consulta"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(JsonHelper.toJson(responseHal))));
    }
}
