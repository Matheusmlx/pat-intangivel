package br.com.azi.patrimoniointangivel.integrationtest.movimentacao;

import br.com.azi.patrimoniointangivel.integrationtest.helper.AuthenticationHelper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarMovimentacaoPorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Transactional
    @Sql({"/datasets/movimentacao/buscar-movimentacao.sql"})
    public void deveBuscarMovimentacaoPorId() throws Exception{
        mockMvc.perform(
            get("/movimentacao/1")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.codigo", equalTo("0001")))
            .andExpect(jsonPath("$.tipo", equalTo("DOACAO_ENTRE_ORGAOS")))
            .andExpect(jsonPath("$.patrimonio", equalTo(1)))
            .andExpect(jsonPath("$.situacao",equalTo("AGUARDANDO_RECEBIMENTO")))
            .andExpect(jsonPath("$.motivo",equalTo("Patrimonio não está mais em uso no orgão atual")))
            .andExpect(jsonPath("$.usuarioCadastro",equalTo("Goku")))
            .andDo(print());
    }

}
