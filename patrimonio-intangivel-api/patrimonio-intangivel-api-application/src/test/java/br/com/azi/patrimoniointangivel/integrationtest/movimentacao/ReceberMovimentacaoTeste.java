package br.com.azi.patrimoniointangivel.integrationtest.movimentacao;

import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniointangivel.domain.entity.Usuario;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReceberMovimentacaoTeste {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioDataProvider usuarioDataProvider;

    @MockBean
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Before
    public void initialize() {
        Mockito.when(usuarioDataProvider.buscarUsuarioPorSessao(any(SessaoUsuario.class))).thenReturn(Usuario
            .builder()
            .nome("nome")
            .id(1L)
            .build());
        Mockito.when(sessaoUsuarioDataProvider.get()).thenReturn(SessaoUsuario
            .builder()
            .id(1L)
            .login("login")
            .build());
    }

    @Test
    @Rollback
    @Transactional
    @Sql({"/datasets/movimentacao/receber-movimentacao.sql"})
    public void deveReceberUmaMovimentacao() throws Exception{
        mockMvc.perform(
            put("/movimentacao/1/receber")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andDo(print());
    }

}
