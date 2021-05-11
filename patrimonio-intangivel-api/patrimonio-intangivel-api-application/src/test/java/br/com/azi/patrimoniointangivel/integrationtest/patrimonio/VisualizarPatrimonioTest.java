package br.com.azi.patrimoniointangivel.integrationtest.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.visualizacao.VisualizarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniointangivel.integrationtest.helper.HalIntegrationHelper;
import br.com.azi.patrimoniointangivel.integrationtest.helper.JsonHelper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/patrimonio/visualizar-patrimonio.sql"})
@Transactional
public class VisualizarPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Before
    public void beforeTest() {
        HalIntegrationHelper.createMockRequisicaoHalSucesso(
            wireMockRule,
            "acesso-verificado-unidade-organizacionais.json",
            "/setup/hal/unidadeOrganizacionalDominio/1/verificarDominioOrgaoPorFuncao?unidadeOrganizacionalId=10000&funcao=Patrimonio.Normal&funcao=Patrimonio.Consulta"
        );
    }

    @Test
    public void deveBuscarVisualizacaoCompletaPatrimonio() throws Exception{
        VisualizarPatrimonioInputData inputData = VisualizarPatrimonioInputData
            .builder()
            .id((long) 1)
            .build();

        mockMvc.perform(
            get("/patrimonios/68/visualizacao-completa")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(68)))
            .andExpect(jsonPath("$.permitirDesativacao", equalTo(false)));
    }
}
