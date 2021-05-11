package br.com.azi.patrimoniointangivel.gateway.dataprovider.converter;

import br.com.azi.patrimoniointangivel.domain.entity.Usuario;
import br.com.azi.patrimoniointangivel.gateway.dataprovider.entity.UsuarioEntity;
import br.com.azi.patrimoniointangivel.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConverter extends GenericConverter<UsuarioEntity, Usuario> {
}
