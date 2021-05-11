-- ######################## Comum ################################

-- Sequences

create sequence comum.seq_usuario;
create sequence comum.seq_produto;
create sequence comum.seq_produto_atributo;

-- Tables

create table comum.tb_usuario(
    us_id bigint not null,
    us_email varchar(255) not null,
    us_email_contato varchar(255) null,
    us_doc_chave varchar(255),
    us_nome varchar(255) not null,
    us_exibir_representacoes boolean,
    us_telefone varchar(255),
    us_imagem varchar(500),
    us_tema varchar(20),
    us_situacao varchar(7),
    us_tipo_usuario varchar(20),
    id_id bigint,
    pa_id bigint not null
);

create table comum.tb_produto(
 pr_id integer not null,
 pr_nome varchar(100) not null,
 pr_descricao varchar(4000),
 pr_logo varchar(4000),
 pr_logo_email varchar(4000),
 pr_titulo_img varchar(4000),
 pr_img_principal varchar(4000),
 pr_css_default varchar(4000),
 pl_id integer,
 pr_dthr_cadastro timestamp(6),
 pr_dthr_alteracao timestamp(6),
 pr_usuario_cadastro varchar(255),
 pr_usuario_alteracao varchar(255)
);

create table comum.tb_produto_atributo(
 pa_id integer not null,
 pa_atributo varchar(255) not null,
 pa_valor varchar(4000) not null,
 pa_tipo varchar(100),
 pr_id integer not null,
 pa_dthr_cadastro timestamp(6),
 pa_dthr_alteracao timestamp(6),
 pa_usuario_cadastro varchar(255),
 pa_usuario_alteracao varchar(255)
);

-- ######################## Comum Siga ##########################

-- Sequences

create sequence comum_siga.seq_conta_contabil;
create sequence comum_siga.seq_pessoa;
create sequence comum_siga.seq_tipo_documento;
create sequence comum_siga.seq_unidade_organizacional;

-- Tables

create table comum_siga.tb_conta_contabil
(
    cc_id                bigint      not null,
    cc_codigo            varchar(16)  not null,
    cc_descricao         varchar(152) not null,
    cc_situacao          varchar(10)  not null,
    cc_dthr_cadastro     timestamp(6),
    cc_dthr_alteracao    timestamp(6),
    cc_usuario_cadastro  varchar(255),
    cc_usuario_alteracao varchar(255)
);

create table comum_siga.tb_conta_contabil_produto
(
    cc_id bigint not null,
    pr_id bigint not null
);

create table comum_siga.tb_pessoa(
    pe_id integer not null,
    pe_nome_razaosocial varchar(100) not null,
    pe_nome_fantasia varchar(100),
    pe_cpf_cnpj varchar(100) not null,
    pe_data_nascimento date,
    pe_data_fundacao date,
    pe_logradouro varchar(255),
    pe_bairro varchar(255),
    pe_numero varchar(20),
    pe_complemento varchar(100),
    pe_cep varchar(20),
    pe_site varchar(255),
    pe_logo varchar(255),
    pe_observacao varchar(4000),
    pe_cidade_estrangeira varchar(100),
    pe_estado_estrangeiro varchar(100),
    pe_cerca varchar(15),
    pe_situacao varchar(10),
    pe_impedido character(1),
    mu_id integer,
    pl_id integer,
    pa_id integer not null,
    pe_numero_cerca integer,
    pe_ano_cerca smallint,
    pe_data_emissao_cerca timestamp(6),
    pe_data_atualizacao_cerca timestamp(6),
    pe_data_validade_cerca timestamp(6)
);

create table comum_siga.tb_tipo_documento
(
    td_id                      integer     not null,
    td_desc                    varchar(50) not null,
    td_permite_anexo           boolean     not null,
    td_dthr_cadastro           timestamp(6),
    td_dthr_alteracao          timestamp(6),
    td_usuario_cadastro        varchar(255),
    td_usuario_alteracao       varchar(255),
    td_identificacao_documento varchar(20) not null
);

create table comum_siga.tb_unidade_organizacional (
    uo_id integer not null,
    uo_nome varchar(250) not null,
    uo_sigla varchar(50) not null,
    uo_situacao varchar(20) not null,
    uo_tipo varchar(10) not null,
    uo_tipo_adm varchar(50) ,
    uo_cod_hierarquia varchar(500) not null,
    uo_responsavel varchar(100),
    uo_substituto varchar(100),
    uo_logradouro varchar(255),
    uo_numero varchar(10),
    uo_complemento varchar(255),
    uo_bairro varchar(255),
    uo_cep varchar(9),
    uo_almoxarifado boolean,
    uo_cod_orgao varchar(20),
    uo_email varchar(200),
    uo_data_decreto timestamp(6),
    uo_numero_decreto varchar(20),
    uo_motivo_alteracao varchar(50),
    uo_justificativa varchar(255),
    uo_id_origem bigint,
    uo_id_externo bigint,
    uo_estrutura_administrativa bigint,
    uo_id_superior bigint,
    mu_id bigint
);

-- ######################## Pat Intangivel#######################

-- Sequences

create sequence pat_intangivel.seq_patrimonio;
create sequence pat_intangivel.seq_config_depreciacao;
create sequence pat_intangivel.seq_documento;
create sequence pat_intangivel.seq_amortizacao;
create sequence pat_intangivel.seq_dados_amortizacao;
create sequence pat_intangivel.seq_config_contacontabil;
create sequence pat_intangivel.seq_movimentacao;
create sequence pat_intangivel.seq_lancamentos_contabeis;
create sequence pat_intangivel.seq_nota_lancto_contabil;

-- Tables

CREATE TABLE pat_intangivel.tb_patrimonio
(
  pa_id Integer NOT NULL,
  pa_tipo Character varying(45) NOT NULL,
  pa_nome Character varying(255),
  pa_numero Character varying(20),
  pa_descricao Text,
  pa_situacao Character varying(45),
  pa_estado Character varying(45),
  pa_valor_liquido Numeric(20,6),
  pa_valor_entrada Numeric(20,6),
  pa_valor_aquisicao Numeric(20,6),
  pa_reconhecimento Character varying(45),
  pa_dthr_aquisicao Timestamp(9),
  pa_dthr_nl Timestamp(9),
  pa_numero_nl Character varying(45),
  pa_dthr_inicio_vida_util Timestamp(9),
  pa_dthr_vencimento Timestamp(9),
  pa_amortizavel Boolean,
  pa_dthr_final_ativacao Timestamp(9),
  pa_ativacao_retroativa Boolean,
  pa_meses_vida_util Smallint,
  pa_dthr_fim_vida_util Timestamp(9),
  pa_dthr_ativacao Timestamp(9),
  pa_vida_indefinida Boolean,
  uo_id_orgao Integer,
  uo_id_setor Integer,
  cc_id Integer,
  pe_id Integer,
  da_id Integer,
  pa_dthr_cadastro Timestamp(9),
  pa_dthr_alteracao Timestamp(9),
  pa_usuario_cadastro Character varying(255),
  pa_usuario_alteracao Character varying(255),
  pa_numero_memorando Character varying(6),
  pa_ano_memorando Character varying(4),
  nlc_id Integer
);

-- Tables

create table pat_intangivel.tb_config_amortizacao
(
    ca_id                  integer               not null,
    ca_metodo              varchar(45),
    ca_vida_util_meses     smallint,
    ca_situacao            varchar(45)           not null,
    ca_taxa                numeric(18, 6),
    ca_percentual_residual numeric(18, 6),
    ca_tipo                varchar(45),
    cc_id                  integer,
    ca_dthr_cadastro       timestamp(6),
    ca_dthr_alteracao      timestamp(6),
    ca_usuario_cadastro    varchar(255),
    ca_usuario_alteracao   varchar(255)
);

CREATE TABLE pat_intangivel.tb_documento
(
  do_id Integer NOT NULL,
  do_numero Character varying(45) NOT NULL,
  do_dt Timestamp(9) NOT NULL,
  do_valor Numeric(20,6) NOT NULL,
  do_uri_anexo Character varying(45) NOT NULL,
  pa_id Integer NOT NULL,
  td_id Integer NOT NULL,
  do_dthr_cadastro Timestamp(9),
  do_dthr_alteracao Timestamp(9),
  do_usuario_cadastro Character varying(255),
  do_usuario_alteracao Character varying(255),
  mo_id Integer
);

CREATE TABLE pat_intangivel.tb_amortizacao
(
  am_id Integer NOT NULL,
  am_dthr_inicial Timestamp(9) NOT NULL,
  am_dthr_final Timestamp(9) NOT NULL,
  am_valor_anterior Numeric(20,6) NOT NULL,
  am_valor_posterior Numeric(20,6) NOT NULL,
  am_valor_subtraido Numeric(20,6) NOT NULL,
  am_taxa_aplicada Numeric(20,6) NOT NULL,
  pa_id Integer NOT NULL,
  ca_id Integer NOT NULL,
  am_dthr_cadastro Timestamp(9),
  am_dthr_alteracao Timestamp(9),
  am_usuario_cadastro Character varying(255),
  am_usuario_alteracao Character varying(255),
  uo_id_orgao          integer
);

CREATE TABLE pat_intangivel.tb_dados_amortizacao
(
  da_id Integer NOT NULL,
  ca_id Integer NOT NULL,
  pa_id Integer NOT NULL,
  da_dthr_cadastro Timestamp(9),
  da_dthr_alteracao Timestamp(9),
  da_usuario_cadastro Character varying(255),
  da_usuario_alteracao Character varying(255)
);


CREATE TABLE pat_intangivel.tb_config_conta_contabil
(
  ccc_id Integer NOT NULL,
  ccc_metodo Character varying(45),
  ccc_tipo Character varying(45) NOT NULL,
  cc_id Integer NOT NULL,
  ccc_dthr_cadastro Timestamp(9),
  ccc_dthr_alteracao Timestamp(9),
  ccc_usuario_cadastro character varying(255),
  ccc_usuario_alteracao character varying(255)
);

CREATE TABLE pat_intangivel.tb_movimentacao
(
    mo_id Integer NOT NULL,
    mo_codigo Character varying(20),
    mo_tipo Character varying (45) NOT NULL,
    uo_id_orgao_origem Integer,
    uo_id_orgao_destino Integer,
    pa_id Integer NOT NULL,
    nlc_id Integer,
    mo_situacao Character varying (45),
    mo_dthr_nl timestamp (9),
    mo_numero_nl Character varying (45),
    mo_dthr_envio timestamp (9),
    mo_motivo_obs Character varying (500),
    mo_motivo_rejeicao Character varying (500),
    mo_dthr_finalizacao timestamp (9),
    mo_dthr_cadastro timestamp(9),
    mo_dthr_alteracao timestamp(9),
    mo_usuario_cadastro Character varying (255),
    mo_usuario_alteracao Character varying(255),
    mo_usuario_finalizacao Character varying(255)
);

CREATE TABLE pat_intangivel.tb_lancamentos_contabeis
(
    lc_id Integer NOT NULL,
    lc_tipo_lancamento Character varying (45) NOT NULL,
    lc_motivo_lancamento Character varying (45) NOT NULL,
    lc_valor_liquido Numeric(20,6),
    lc_data timestamp(9),
    uo_id_orgao Integer,
    cc_id Integer,
    mo_id Integer,
    pa_id Integer,
    lc_dthr_cadastro timestamp(9),
    lc_dthr_alteracao timestamp(9),
    lc_usuario_cadastro Character varying (255),
    lc_usuario_alteracao Character varying (255)
);

CREATE TABLE pat_intangivel.tb_nota_lancto_contabil
(
    nlc_id Integer NOT NULL,
    nlc_numero Character varying(45),
    nlc_dthr_lancamento Timestamp(9),
    nlc_dthr_cadastro Timestamp (9),
    nlc_dthr_alteracao Timestamp (9),
    nlc_usuario_cadastro Character varying(255),
    nlc_usuario_alteracao Character varying(255)
);

-- Constraints

ALTER TABLE pat_intangivel.tb_documento ADD CONSTRAINT fk_patrimonio_documento FOREIGN KEY (pa_id) REFERENCES pat_intangivel.tb_patrimonio  (pa_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_patrimonio  ADD CONSTRAINT fk_unidadeorg_patrimonio FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_dados_amortizacao ADD CONSTRAINT fk_configdeprec_dadosamort FOREIGN KEY (ca_id) REFERENCES pat_intangivel.tb_config_amortizacao (ca_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_amortizacao ADD CONSTRAINT fk_patrimonio_amortizado FOREIGN KEY (pa_id) REFERENCES pat_intangivel.tb_patrimonio  (pa_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_documento ADD CONSTRAINT fk_tipodoc_documento FOREIGN KEY (td_id) REFERENCES comum_siga.tb_tipo_documento (td_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_patrimonio  ADD CONSTRAINT fk_pessoa_patrimonio FOREIGN KEY (pe_id) REFERENCES comum_siga.tb_pessoa (pe_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_dados_amortizacao ADD CONSTRAINT fk_patrimonio_dadosamort FOREIGN KEY (pa_id) REFERENCES pat_intangivel.tb_patrimonio  (pa_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_patrimonio  ADD CONSTRAINT fk_conta_patrimonio FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_amortizacao ADD CONSTRAINT fk_configdepre_amortizacao FOREIGN KEY (ca_id) REFERENCES pat_intangivel.tb_config_amortizacao (ca_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_patrimonio  ADD CONSTRAINT fk_dadosamort_patrimonio FOREIGN KEY (da_id) REFERENCES pat_intangivel.tb_dados_amortizacao (da_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_patrimonio  ADD CONSTRAINT fk_uo_patrimonio_setor FOREIGN KEY (uo_id_setor) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_config_conta_contabil ADD CONSTRAINT fk_contacontab_configcontcont FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_lancamentos_contabeis ADD CONSTRAINT fk_unidadeorg_lancamentocontabil FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_lancamentos_contabeis ADD CONSTRAINT fk_contacontab_lancamentocontabil FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (cc_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_lancamentos_contabeis ADD CONSTRAINT fk_movimentacao_lancamentocontabil FOREIGN KEY (mo_id) REFERENCES pat_intangivel.tb_movimentacao (mo_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_lancamentos_contabeis ADD CONSTRAINT fk_patrimonio_lancamentocontabil FOREIGN KEY (pa_id) REFERENCES pat_intangivel.tb_patrimonio (pa_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_amortizacao  ADD CONSTRAINT fk_unidadeorg_amortizacao FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.tb_unidade_organizacional (uo_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_patrimonio  ADD CONSTRAINT fk_nlc_patrimonio FOREIGN KEY (nlc_id) REFERENCES pat_intangivel.tb_nota_lancto_contabil (nlc_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_movimentacao  ADD CONSTRAINT fk_nlc_movimentacao FOREIGN KEY (nlc_id) REFERENCES pat_intangivel.tb_nota_lancto_contabil (nlc_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE pat_intangivel.tb_documento ADD CONSTRAINT  fk_movimentacao_documento FOREIGN  KEY (mo_id) REFERENCES pat_intangivel.tb_movimentacao (mo_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
