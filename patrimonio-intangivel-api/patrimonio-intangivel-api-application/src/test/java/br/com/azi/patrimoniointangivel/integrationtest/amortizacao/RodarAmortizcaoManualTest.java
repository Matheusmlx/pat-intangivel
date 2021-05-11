package br.com.azi.patrimoniointangivel.integrationtest.amortizacao;


import br.com.azi.patrimoniointangivel.domain.usecase.amortizacao.iniciaamortizacaomanual.IniciaAmortizacaoManualInputData;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/amortizacao/amortizar-manual.sql"})
@Transactional
public class RodarAmortizcaoManualTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRodarAmortizacaoManual() throws Exception{
        IniciaAmortizacaoManualInputData inputData = IniciaAmortizacaoManualInputData
            .builder()
            .orgao(10000L)
            .mes("03")
            .ano("2020")
            .build();

        mockMvc.perform(
            post("/amortizacao-manual")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }
}
