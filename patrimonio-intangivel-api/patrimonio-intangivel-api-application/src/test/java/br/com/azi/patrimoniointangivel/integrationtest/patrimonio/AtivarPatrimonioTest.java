package br.com.azi.patrimoniointangivel.integrationtest.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.ativacao.ativar.AtivarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniointangivel.integrationtest.helper.JsonHelper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AtivarPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql({"/datasets/patrimonio/ativar-patrimonio.sql"})
    @Transactional
    public void deveAtivarPatrimonio() throws Exception{


        AtivarPatrimonioInputData inputData = AtivarPatrimonioInputData
            .builder()
            .id(1L)
            .build();

        mockMvc.perform(
            patch("/patrimonios/1")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.numero", equalTo("0000000001")));
    }

}
