package br.com.azi.patrimoniointangivel.integrationtest.movimentacao;

import br.com.azi.patrimoniointangivel.domain.usecase.movimentacao.edicao.EditarMovimentacaoInputData;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EditarMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Transactional
    @Sql({"/datasets/movimentacao/editar-movimentacao.sql"})
    public void deveEditarMovimentacao() throws Exception{

        EditarMovimentacaoInputData inputData = EditarMovimentacaoInputData
            .builder()
            .id(1L)
            .motivo("O orgão esta precisando desse patrimonio para seu uso")
            .build();

        mockMvc.perform(
            put("/movimentacao/1")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}
