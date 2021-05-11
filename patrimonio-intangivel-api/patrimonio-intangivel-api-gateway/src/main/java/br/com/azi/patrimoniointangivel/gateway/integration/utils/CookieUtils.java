package br.com.azi.patrimoniointangivel.gateway.integration.utils;

import br.com.azi.patrimoniointangivel.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniointangivel.domain.gateway.integration.exception.BuscarUnidadesOrganizacionaisException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CookieUtils {

    private static final String COOKIE_AUTENTICACAO_NOME = "JSESSIONID";

    public static String getCookieAutenticacao(SessaoUsuario sessaoUsuario) {
        Optional<SessaoUsuario.Cookie> cookieAutenticacao = sessaoUsuario.getCookies()
            .stream()
            .filter(cookie -> COOKIE_AUTENTICACAO_NOME.equals(cookie.getNome()))
            .findFirst();

        SessaoUsuario.Cookie cookieEncontrado = cookieAutenticacao.orElseThrow(() -> new BuscarUnidadesOrganizacionaisException("Usuario não autenticado."));

        return String.format("%s=%s", cookieEncontrado.getNome(), cookieEncontrado.getValor());
    }

}
