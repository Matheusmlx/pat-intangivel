package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniointangivel.domain.entity.Usuario;

public interface UsuarioDataProvider {
    Usuario buscarUsuarioPorLogin(String login);

    Usuario buscarUsuarioPorSessao(SessaoUsuario sessaoUsuario);
}
