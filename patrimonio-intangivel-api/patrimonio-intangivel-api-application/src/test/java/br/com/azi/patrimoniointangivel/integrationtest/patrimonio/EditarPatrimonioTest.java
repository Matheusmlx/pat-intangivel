package br.com.azi.patrimoniointangivel.integrationtest.patrimonio;


import br.com.azi.patrimoniointangivel.domain.usecase.patrimonio.edicao.EditarPatrimonioInputData;
import br.com.azi.patrimoniointangivel.integrationtest.helper.JsonHelper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EditarPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Sql({"/datasets/patrimonio/edita-patrimonio.sql"})
    public void deveEditarPatrimonio() throws Exception{

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(2L)
            .tipo("SOFTWARES")
            .situacao("EM_ELABORACAO")
            .estado("EM_DESENVOLVIMENTO")
            .nome("Office")
            .build();

        mockMvc.perform(
            put("/patrimonios/2")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andExpect(jsonPath("$.estado", equalTo("EM_DESENVOLVIMENTO")))
            .andExpect(jsonPath("$.nome", equalTo("Office")))
            .andExpect(jsonPath("$.contaContabil.codigo", equalTo("124110200")));
    }

}
