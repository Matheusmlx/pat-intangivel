package br.com.azi.hal.core.generic.entity;

import br.com.azi.hal.core.generic.entity.listener.AuditListener;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class BaseObject implements Serializable {

    @Column(columnDefinition = "TIMESTAMP")
    protected Date dataCadastro;

    @Column(columnDefinition = "TIMESTAMP")
    protected Date dataAlteracao;

    protected String usuarioCadastro;

    protected String usuarioAlteracao;
}
