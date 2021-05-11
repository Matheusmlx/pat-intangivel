package br.com.azi.patrimoniointangivel.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniointangivel.domain.entity.Usuario;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.UsuarioDataProvider;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.converter.UsuarioConverter;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.repository.UsuarioReadOnlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDataProviderImpl implements UsuarioDataProvider {

    @Autowired
    private UsuarioReadOnlyRepository usuarioReadOnlyRepository;

    @Autowired
    private UsuarioConverter usuarioConverter;

    @Override
    public Usuario buscarUsuarioPorLogin(String login) {
        return usuarioConverter.to(usuarioReadOnlyRepository.findByEmail(login));
    }

    @Override
    public Usuario buscarUsuarioPorSessao(SessaoUsuario sessaoUsuario) {
        return this.buscarUsuarioPorLogin(sessaoUsuario.getLogin());
    }
}
