package br.com.azi.patrimoniointangivel.integrationtest.patrimonio;

import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.cadastro.CadastraPatrimonioInputData;
import br.com.azi.patrimoniointangivel.integrationtest.helper.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CadastraPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Transactional
    public void deveSalvarTipoDePatrimonio() throws Exception{
        CadastraPatrimonioInputData inputData = CadastraPatrimonioInputData
                .builder()
                .tipo("SOFTWARES")
                .build();

        mockMvc.perform(
                post("/patrimonios")
                        .content(JsonHelper.toJson(inputData))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.tipo", equalTo("SOFTWARES")))
                .andExpect(jsonPath("$.situacao", equalTo("EM_ELABORACAO")))
                .andExpect(jsonPath("$.nome", isEmptyOrNullString()));
    }
}
