package br.com.azi.patrimoniointangivel.integrationtest.configuracao.contascontabeis;

import br.com.azi.patrimoniointangivel.domain.usecase.configuracao.contacontabil.buscarporid.BuscarContaContabilPorIdInputData;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/configuracao/buscar-conta-contabil.sql"})
@Transactional
public class BuscarContaContabilPorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarContaContabilPorId() throws Exception{

        BuscarContaContabilPorIdInputData inputData = BuscarContaContabilPorIdInputData
            .builder()
            .id(1L)
            .produtoId(1L)
            .build();

        mockMvc.perform(
            get("/configuracao/contas-contabeis/1")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)));
    }

}
