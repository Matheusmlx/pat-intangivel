package br.com.azi.patrimoniointangivel.integrationtest.configuracao.contascontabeis;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.editar.EditarContaContabilInputData;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EditarConfigContaContabilTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Sql({"/datasets/configuracao/editar-config-conta-contabil.sql"})
    @Transactional
    public void deveEditarConfigContaContabil() throws Exception{

        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .tipo("AMORTIZAVEL")
            .contaContabil(1L)
            .build();

        mockMvc.perform(
            post("/configuracao/contas-contabeis/1/config-amortizacao/1")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andDo(print());
    }
}
