package br.com.azi.patrimoniointangivel.integrationtest.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.desativar.DesativarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniointangivel.integrationtest.helper.FileHelper;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/patrimonio/desativar-patrimonio.sql"})
@Transactional
public class DesativarPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    public void deveDesativarPatrimonio() throws Exception{

        createMockRequisicaoBuscarContaContabilPorCodigoSetup();

        DesativarPatrimonioInputData inputData = DesativarPatrimonioInputData
            .builder()
            .id((long) 1)
            .build();

        mockMvc.perform(
            patch("/patrimonios/68/desativar")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andExpect(jsonPath("$.id", equalTo(68)));
    }

    private void createMockRequisicaoBuscarContaContabilPorCodigoSetup() throws IOException {
        String response = FileHelper.getJson("setup", "conta-contabil-desenvolvimento.json");

        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/contaContabil/detalhes?codigo=124110200"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }

}
