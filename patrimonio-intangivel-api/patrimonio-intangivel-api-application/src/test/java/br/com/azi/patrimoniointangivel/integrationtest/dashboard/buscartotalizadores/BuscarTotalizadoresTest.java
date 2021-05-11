package br.com.azi.patrimoniointangivel.integrationtest.dashboard.buscartotalizadores;

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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarTotalizadoresTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-totalizadores.sql"})
    @Transactional
    public void buscarTotalizadoresSemUO() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of();

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/totalizadores")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalDeRegistros", is(1)))
            .andExpect(jsonPath("$.emElaboracao", is(1)))
            .andExpect(jsonPath("$.ativos", is(0)));
    }

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-totalizadores.sql"})
    @Transactional
    public void buscarTotalizadoresComUmaUO() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of(
            UnidadeOrganizacional
                .builder()
                .id(200L)
                .build()
        );

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/totalizadores")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalDeRegistros", is(4)))
            .andExpect(jsonPath("$.emElaboracao", is(2)))
            .andExpect(jsonPath("$.ativos", is(2)));
    }

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-totalizadores.sql"})
    @Transactional
    public void buscarTotalizadoresComDuasUO() throws Exception {
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
            get("/dashboard/totalizadores")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalDeRegistros", is(6)))
            .andExpect(jsonPath("$.emElaboracao", is(3)))
            .andExpect(jsonPath("$.ativos", is(3)));
    }

    private void createMockRequisicaoHal(List responseHal) {
        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/unidadeOrganizacionalDominio/1/buscarOrgaosPorFuncao?funcao=Patrimonio.Normal&funcao=Patrimonio.Consulta"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(JsonHelper.toJson(responseHal))));
    }
}
