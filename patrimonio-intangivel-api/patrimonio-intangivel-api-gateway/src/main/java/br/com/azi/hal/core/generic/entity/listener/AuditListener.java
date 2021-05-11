package br.com.azi.hal.core.generic.entity.listener;

import br.com.azi.hal.core.generic.entity.BaseObject;
import br.com.azi.patrimoniointangivel.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniointangivel.utils.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;
import java.util.Optional;

@Component
public class AuditListener {


    private static final String USUARIO_JOB = "JobSystem";

    @Autowired
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @PreUpdate
    public void auditUpdate(BaseObject entity) {
        Optional.ofNullable(sessaoUsuarioDataProvider.getLogin()).filter(StringUtils::isNotEmpty)
            .ifPresentOrElse(entity::setUsuarioAlteracao, () -> entity.setUsuarioAlteracao(USUARIO_JOB));

        entity.setDataAlteracao(new Date());
    }

    @PrePersist
    public void auditPersist(BaseObject entity) {
        Optional.ofNullable(sessaoUsuarioDataProvider.getLogin()).filter(StringUtils::isNotEmpty)
            .ifPresentOrElse(entity::setUsuarioCadastro, () -> entity.setUsuarioCadastro(USUARIO_JOB));

        entity.setDataCadastro(new Date());
        entity.setDataAlteracao(new Date());
    }
}
