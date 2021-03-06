package br.com.azi.patrimoniointangivel.integrationtest.configuracao.contascontabeis;


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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/configuracao/buscar-listagem-contas-contabeis.sql"})
@Transactional
public class BuscarContasContabeisTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarContasContabeis() throws Exception{


        mockMvc.perform(
            get("/configuracao/contas-contabeis?page=1&size=10&sort=codigo&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful());
    }
}
