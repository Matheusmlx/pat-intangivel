package br.com.azi.patrimoniointangivel.integrationtest.dashboard.buscarproximospatrimoniosavencer;

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
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarProximosPatrimoniosAVencerTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-proximos-patrimonios-a-vencer.sql"})
    @Transactional
    public void BuscarProximosPatrimoniosAVencerComUsuarioQueNaoEstaVinculadoANenhumOrgao() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of();

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/licenca/proximas-vencer")
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
    @Sql({"/datasets/dashboard/buscar-proximos-patrimonios-a-vencer.sql"})
    @Transactional
    public void BuscarProximosPatrimoniosAVencerComUsuarioVinculadoAUmOrgao() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of(
            UnidadeOrganizacional
                .builder()
                .id(200L)
                .build()
        );

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/licenca/proximas-vencer")
                .param("quantidade", "5")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.itens", hasSize(2)))
            .andExpect(jsonPath("$.itens[0].id", is(12)))
            .andExpect(jsonPath("$.itens[0].nome", is("Office 12")))
            .andExpect(jsonPath("$.itens[0].diasParaVencer", notNullValue()))

            .andExpect(jsonPath("$.itens.[1].id", is(13)))
            .andExpect(jsonPath("$.itens.[1].nome", is("Office 13")))
            .andExpect(jsonPath("$.itens.[1].diasParaVencer", notNullValue()));
    }

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-proximos-patrimonios-a-vencer.sql"})
    @Transactional
    public void BuscarProximosPatrimoniosAVencerComUsuarioVinculadoADoisOrgao() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of(
            UnidadeOrganizacional
                .builder()
                .id(100L)
                .build(),
            UnidadeOrganizacional
                .builder()
                .id(200L)
                .build());

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/licenca/proximas-vencer")
                .param("quantidade", "5")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.itens", hasSize(3)))
            .andExpect(jsonPath("$.itens[0].id", is(10)))
            .andExpect(jsonPath("$.itens[0].nome", is("Office 10")))
            .andExpect(jsonPath("$.itens[0].diasParaVencer", notNullValue()))

            .andExpect(jsonPath("$.itens.[1].id", is(12)))
            .andExpect(jsonPath("$.itens.[1].nome", is("Office 12")))
            .andExpect(jsonPath("$.itens.[1].diasParaVencer", notNullValue()))

            .andExpect(jsonPath("$.itens.[2].id", is(13)))
            .andExpect(jsonPath("$.itens.[2].nome", is("Office 13")))
            .andExpect(jsonPath("$.itens.[2].diasParaVencer", notNullValue()));
    }

    @Test
    @Rollback
    @Sql({"/datasets/dashboard/buscar-proximos-patrimonios-a-vencer.sql"})
    @Transactional
    public void BuscarProximosPatrimoniosAVencerSemPassarQuantidade() throws Exception {
        List<UnidadeOrganizacional> responseHal = List.of();

        createMockRequisicaoHal(responseHal);

        mockMvc.perform(
            get("/dashboard/licenca/proximas-vencer")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is4xxClientError())
            .andReturn().getResolvedException();
    }

    private void createMockRequisicaoHal(List responseHal) {
        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/unidadeOrganizacionalDominio/1/buscarOrgaosPorFuncao?funcao=Patrimonio.Normal&funcao=Patrimonio.Consulta"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(JsonHelper.toJson(responseHal))));
    }
}
