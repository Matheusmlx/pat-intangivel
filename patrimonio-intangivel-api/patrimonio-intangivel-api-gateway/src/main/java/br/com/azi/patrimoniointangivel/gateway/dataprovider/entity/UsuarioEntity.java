package br.com.azi.patrimoniointangivel.gateway.dataprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_USUARIO", schema = "COMUM")
public class UsuarioEntity {
    @Id
    @Column(name = "US_ID", insertable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "US_EMAIL", nullable = false, insertable = false, updatable = false)
    private String email;

    @Column(name = "US_EMAIL_CONTATO", insertable = false, updatable = false)
    private String emailContato;

    @Column(name = "US_DOC_CHAVE", nullable = false, insertable = false, updatable = false)
    private String docChave;

    @Column(name = "US_NOME", nullable = false, insertable = false, updatable = false)
    private String nome;

    @Column(name = "US_TELEFONE", insertable = false, updatable = false)
    private String telefone;

    @Column(name = "US_SITUACAO", insertable = false, updatable = false)
    private String situacao;

    @Column(name = "US_TIPO_USUARIO", insertable = false, updatable = false)
    private String tipoUsuario;

}
