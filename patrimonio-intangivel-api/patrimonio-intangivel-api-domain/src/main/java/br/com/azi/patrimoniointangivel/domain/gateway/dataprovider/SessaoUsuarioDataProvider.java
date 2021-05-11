package br.com.azi.patrimoniointangivel.domain.gateway.dataprovider;

import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;

public interface SessaoUsuarioDataProvider {

    SessaoUsuario get();

    String getLogin();
}
