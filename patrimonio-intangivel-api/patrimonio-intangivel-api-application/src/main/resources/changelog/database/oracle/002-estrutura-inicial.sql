CREATE SEQUENCE pat_intangivel.seq_patrimonio
    START WITH 1
    INCREMENT BY 1
    NOMINVALUE
    NOMAXVALUE
    NOCACHE;

CREATE SEQUENCE pat_intangivel.seq_config_depreciacao
    START WITH 1
    INCREMENT BY 1
    NOMINVALUE
    NOMAXVALUE
    NOCACHE;

CREATE SEQUENCE pat_intangivel.seq_documento
    START WITH 1
    INCREMENT BY 1
    NOMINVALUE
    NOMAXVALUE
    NOCACHE;

CREATE SEQUENCE pat_intangivel.seq_amortizacao
    START WITH 1
    INCREMENT BY 1
    NOMINVALUE
    NOMAXVALUE
    NOCACHE;

CREATE SEQUENCE pat_intangivel.seq_dados_amortizacao
    START WITH 1
    INCREMENT BY 1
    NOMINVALUE
    NOMAXVALUE
    NOCACHE;


CREATE SEQUENCE pat_intangivel.seq_config_contacontabil
  START WITH 1
  INCREMENT BY 1
  NOMAXVALUE
  NOMINVALUE
  NOCACHE;


-- Create tables section -------------------------------------------------

-- Table  pat_intangivel.tb_patrimonio

CREATE TABLE pat_intangivel.tb_patrimonio(
  pa_id Integer NOT NULL,
  pa_tipo Varchar2(45 ) NOT NULL,
  pa_nome Varchar2(255 ),
  pa_numero Varchar2(20 ),
  pa_descricao Clob,
  pa_situacao Varchar2(45 ),
  pa_estado Varchar2(45 ),
  pa_valor_liquido Number(20,6),
  pa_valor_aquisicao Number(20,6),
  pa_reconhecimento Varchar2(45 ),
  pa_dthr_aquisicao Timestamp(9),
  pa_dthr_nl Timestamp(9),
  pa_numero_nl Varchar2(45 ),
  pa_dthr_inicio_vida_util Timestamp(9),
  pa_dthr_vencimento Timestamp(9),
  pa_meses_vida_util Integer,
  pa_dthr_vida_util Timestamp(9),
  pa_dthr_ativacao Timestamp(9),
  pa_amortizavel Number (1, 0) default 0,
  pa_dthr_final_ativacao Timestamp(9),
  pa_vida_indefinida Number (1, 0) default 0,
  pa_ativacao_retroativa Number (1, 0) default 0,
  uo_id_orgao Integer,
  uo_id_setor Integer,
  cc_id Integer,
  pe_id Integer,
  da_id Integer,
  pa_dthr_cadastro Timestamp(9),
  pa_dthr_alteracao Timestamp(9),
  pa_usuario_cadastro Varchar2(255),
  pa_usuario_alteracao Varchar2(255)
);

-- Create indexes for table  pat_intangivel.tb_patrimonio

CREATE INDEX in_pa_uo_id_orgao ON pat_intangivel.tb_patrimonio (uo_id_orgao);

CREATE INDEX in_pa_pe_id ON pat_intangivel.tb_patrimonio (pe_id);

CREATE INDEX in_pa_cc_id ON pat_intangivel.tb_patrimonio (cc_id);

CREATE INDEX in_pa_da_id ON pat_intangivel.tb_patrimonio (da_id);

CREATE INDEX in_pa_uo_id_setor ON pat_intangivel.tb_patrimonio (uo_id_setor);

-- Add keys for table  pat_intangivel.tb_patrimonio

ALTER TABLE pat_intangivel.tb_patrimonio ADD CONSTRAINT pk_tb_patrimonio PRIMARY KEY (pa_id);

-- Table and Columns comments section

COMMENT ON TABLE pat_intangivel.tb_patrimonio IS 'Cadastro dos patrim??nios intang??veis.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_tipo IS 'Tipo de bem intang??vel. Pode assumir os valores: SOFTWARE, DIREITOS_AUTORAIS, LICENCAS, MARCAS, TITULOS_DE_PUBLICACAO, RECEITAS_FORMULAS_PROJETOS.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_nome IS 'Nome do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_numero IS 'Numero do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_descricao IS 'Descricao do patrimonio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_situacao IS 'Situa????o do patrim??nio. Pode assumir os valores: EM_ELABORACAO, ATIVO e BAIXADO.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_estado IS 'Estado do bem intang??vel. Pode assumir os valores: PRONTO_PARA_USO e EM_DESENVOLVIMENTO.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_valor_liquido IS 'Valor liquido atual do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_valor_aquisicao IS 'Valor de aquisi????o final do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_reconhecimento IS 'A????o pela qual ?? definido como o intang??vel foi adquirido. Pode assumir os valores: AQUISICAO_SEPARADA, GERACAO_INTERNA e TRANSACAO_SEM_CONTRAPRESTACAO.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_dthr_aquisicao IS 'Data de aquisi????o do bem.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_dthr_nl IS 'Data da nota de lan??amento.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_numero_nl IS 'N??mero da nota de lan??amento do bem.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_dthr_inicio_vida_util IS 'Data de in??cio da vida ??til do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_dthr_vencimento IS 'Data de vencimento na qual o bem intangivel deixa de ter valor, ou a licen??a de um software expira.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_meses_vida_util IS 'Quantidade de meses de vida ??til do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_dthr_vida_util IS 'Data em que se encerra a vida ??til do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio .pa_vida_indefinida IS 'Valor booleano para indicar se patrim??nio possui vida util definida.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio .pa_amortizavel IS 'Valor booleano para indicar se patrim??nio ?? amortiz??vel.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio .pa_dthr_final_ativacao IS 'Data/Hora de ativa????o do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio .pa_ativacao_retroativa IS 'Valor booleano para indicar se patrim??nio possui ativa????o retroativa.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.uo_id_orgao IS 'Chave estrangeira para o org??o a qual o patrim??nio pertence.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.uo_id_setor IS 'Chave estrangeira para o setor a qual o patrim??nio pertence.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.cc_id IS 'Chave estrangeira para a conta cont??bil do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pe_id IS 'Chave estrangeira para o fornecedorl do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.da_id IS 'Chave estrangeira para a configuracao de depreciacaol do patrim??nio.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_dthr_cadastro IS 'Data/Hora de cria????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_dthr_alteracao IS 'Data/Hora da ??ltima altera????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_usuario_cadastro IS 'Usu??rio que criou o registro.';

COMMENT ON COLUMN pat_intangivel.tb_patrimonio.pa_usuario_alteracao IS '??ltimo usu??rio a alterar o registro.';


-- Table tb_config_amortizacao

CREATE TABLE pat_intangivel.tb_config_amortizacao(
  ca_id Integer NOT NULL,
  ca_metodo Varchar2(45),
  ca_vida_util_meses Integer,
  ca_situacao Varchar2(45) NOT NULL,
  ca_taxa Number(12,6),
  ca_percentual_residual Number(12,6),
  ca_tipo Varchar2(45) NOT NULL,
  cc_id Integer NOT NULL,
  ca_dthr_cadastro timestamp(9),
  ca_dthr_alteracao timestamp(9),
  ca_usuario_cadastro varchar2(255),
  ca_usuario_alteracao varchar2(255)
);

-- Create indexes for table tb_config_amortizacao

CREATE INDEX IN_CD_CC_ID ON pat_intangivel.tb_config_amortizacao (cc_id);

-- Add keys for table tb_config_amortizacao

ALTER TABLE pat_intangivel.tb_config_amortizacao ADD CONSTRAINT PK_tb_config_amortizacao PRIMARY KEY (CD_ID);

-- Table and Columns comments section

COMMENT ON TABLE pat_intangivel.tb_config_amortizacao IS 'Dados para c??lculo de amortiza????es (intang??veisbenfeitorias) e deprecia????es (ativos imobilizados). ';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_metodo IS 'M??todo utilizado para calcular a amortiza????odeprecia????o.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_vida_util_meses IS 'Vida util do bem em meses.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_situacao IS 'Situa????o dos dados de amortiza????odeprecia????o da conta cont??bil. Pode assumir os valores:';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_taxa IS 'Taxa de amortiza????o para conta cont??bil.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_percentual_residual IS 'Valor percentual que o patrim??nio ter?? ap??s o fim da vida ??til.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_tipo IS 'Tipo de opera????o de desvaloriza????o, pode ser deprecia????o, amortiza????o ou exaust??o.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.cc_id IS 'Chave estrangeira para conta cont??bil.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_dthr_cadastro IS 'Data/Hora de cria????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_dthr_alteracao IS 'Data/Hora da ??ltima altera????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_usuario_cadastro IS 'Usu??rio que criou o registro.';

COMMENT ON COLUMN pat_intangivel.tb_config_amortizacao.ca_usuario_alteracao IS '??ltimo usu??rio a alterar o registro.';

-- Table TB_DOCUMENTO

CREATE TABLE pat_intangivel.tb_documento(
  do_id Integer NOT NULL,
  do_numero Varchar2(45 ) NOT NULL,
  do_dt Timestamp(9) NOT NULL,
  do_valor Number(20,6) NOT NULL,
  do_uri_anexo Varchar2(500 ) NOT NULL,
  pa_id Integer NOT NULL,
  td_id Integer NOT NULL,
  do_dthr_cadastro Timestamp(9),
  do_dthr_alteracao Timestamp(9),
  do_usuario_cadastro Varchar2(255 ),
  do_usuario_alteracao Varchar2(255 )
);

-- Create indexes for table TB_DOCUMENTO

CREATE INDEX in_do_pa_id ON pat_intangivel.tb_documento (pa_id);

CREATE INDEX in_do_td_id ON pat_intangivel.tb_documento (td_id);

-- Add keys for table TB_DOCUMENTO

ALTER TABLE pat_intangivel.tb_documento ADD CONSTRAINT pk_tb_documento PRIMARY KEY (do_id);

-- Table and Columns comments section

COMMENT ON TABLE pat_intangivel.tb_documento IS 'Documentos referentes ao bem.';

COMMENT ON COLUMN pat_intangivel.tb_documento.do_numero IS 'N??mero de identifica????o do documento.';

COMMENT ON COLUMN pat_intangivel.tb_documento.do_dt IS 'Data do documento.';

COMMENT ON COLUMN pat_intangivel.tb_documento.do_valor IS 'Valor do dcocumento.';

COMMENT ON COLUMN pat_intangivel.tb_documento.do_uri_anexo IS 'Caminho para o documento anexado.';

COMMENT ON COLUMN pat_intangivel.tb_documento.pa_id IS 'Chave estrangeira para o patrimonio.';

COMMENT ON COLUMN pat_intangivel.tb_documento.td_id IS 'Chave estrangeira para o tipo de documento.';

COMMENT ON COLUMN pat_intangivel.tb_documento.do_dthr_cadastro IS 'Data/Hora de cria????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_documento.do_dthr_alteracao IS 'Data/Hora da ??ltima altera????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_documento.do_usuario_cadastro IS 'Usu??rio que criou o registro.';

COMMENT ON COLUMN pat_intangivel.tb_documento.do_usuario_alteracao IS '??ltimo usu??rio a alterar o registro.';

-- Table TB_AMORTIZACAO

CREATE TABLE pat_intangivel.tb_amortizacao(
  am_id Integer NOT NULL,
  am_dthr_inicial Timestamp(9) NOT NULL,
  am_dthr_final Timestamp(9) NOT NULL,
  am_valor_anterior Number(20,6) NOT NULL,
  am_valor_posterior Number(20,6) NOT NULL,
  am_valor_subtraido Number(20,6) NOT NULL,
  am_taxa_aplicada Numeric(20,6) NOT NULL,
  pa_id Integer NOT NULL,
  ca_id Integer NOT NULL,
  am_dthr_cadastro Timestamp(9),
  am_dthr_alteracao Timestamp(9),
  am_usuario_cadastro Varchar2(255 ),
  am_usuario_alteracao Varchar2(255 )
);

-- Create indexes for table TB_AMORTIZACAO

CREATE INDEX in_am_pa_id ON pat_intangivel.tb_amortizacao (pa_id);

CREATE INDEX in_am_ca_id ON pat_intangivel.tb_amortizacao (ca_id);

-- Add keys for table TB_AMORTIZACAO

ALTER TABLE pat_intangivel.tb_amortizacao ADD CONSTRAINT pk_tb_amortizacao PRIMARY KEY (am_id);

-- Table and Columns comments section

COMMENT ON TABLE pat_intangivel.tb_amortizacao IS 'Amortiza????es realizadas nos Patrim??nios.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_id IS 'Chave prim??ria.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_dthr_inicial IS 'Data no m??s a partir da qual o bem come??ou a amortiza????o.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_dthr_final IS 'Data final em determinado m??s usada para calcular a amortiza????o mensal do bem.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_valor_anterior IS 'Valor liquido que o bem possui antes da amortiza????o mensal.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_valor_posterior IS 'Valor liquido do bem ap??s a amortiza????o mensal.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_valor_subtraido IS 'Valor mensal amortizado no bem.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_taxa_aplicada IS 'Taxa de amortiza????o refrente ao per??odo aplicado.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.pa_id IS 'Chave estrangeira para o patrimonio que amortizou.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.ca_id IS 'Chave estrangeira para o conta contabil que amortizou.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_dthr_cadastro IS 'Data/Hora de cria????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_dthr_alteracao IS 'Data/Hora da ??ltima altera????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_usuario_cadastro IS 'Usu??rio que criou o registro.';

COMMENT ON COLUMN pat_intangivel.tb_amortizacao.am_usuario_alteracao IS '??ltimo usu??rio a alterar o registro.';

-- Table TB_DADOS_AMORTIZACAO

CREATE TABLE pat_intangivel.tb_dados_amortizacao(
  da_id Integer NOT NULL,
  ca_id Integer NOT NULL,
  pa_id Integer NOT NULL,
  da_dthr_cadastro Timestamp(9),
  da_dthr_alteracao Timestamp(9),
  da_usuario_cadastro Varchar2(255 ),
  da_usuario_alteracao Varchar2(255 )
);

-- Create indexes for table TB_DADOS_AMORTIZACAO

CREATE INDEX in_da_ca_id ON pat_intangivel.tb_dados_amortizacao (ca_id);

CREATE INDEX in_da_pa_id ON pat_intangivel.tb_dados_amortizacao (pa_id);

-- Add keys for table TB_DADOS_AMORTIZACAO

ALTER TABLE pat_intangivel.tb_dados_amortizacao ADD CONSTRAINT pk_tb_dados_amortizacao PRIMARY KEY (da_id);

-- Table and Columns comments section

COMMENT ON TABLE pat_intangivel.tb_dados_amortizacao IS 'Rela????o entre o Patrim??nio e os Dados de Amortiza????o.';

COMMENT ON COLUMN pat_intangivel.tb_dados_amortizacao.ca_id IS 'Chave estrangeira para a conta cont??bil.';

COMMENT ON COLUMN pat_intangivel.tb_dados_amortizacao.pa_id IS 'Chave estrangeira para a patrimonio.';

COMMENT ON COLUMN pat_intangivel.tb_dados_amortizacao.da_dthr_cadastro IS 'Data/Hora de cria????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_dados_amortizacao.da_dthr_alteracao IS 'Data/Hora da ??ltima altera????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_dados_amortizacao.da_usuario_cadastro IS 'Usu??rio que criou o registro.';

COMMENT ON COLUMN pat_intangivel.tb_dados_amortizacao.da_usuario_alteracao IS '??ltimo usu??rio a alterar o registro.';


-- Table pat_intangivel.tb_config_conta_contabil


CREATE TABLE pat_intangivel.tb_config_conta_contabil
(
  ccc_id Integer NOT NULL,
  ccc_metodo Varchar2(45),
  ccc_tipo Varchar2(45) NOT NULL,
  cc_id Integer NOT NULL,
  ccc_dthr_cadastro Timestamp(9),
  ccc_dthr_alteracao Timestamp(9),
  ccc_usuario_cadastro Varchar2(255 ),
  ccc_usuario_alteracao Varchar2(255 )
);

-- Create indexes for table tb_config_conta_contabil

CREATE INDEX in_ccc_cc_id ON pat_intangivel.tb_config_conta_contabil (cc_id);

-- Add keys for table tb_config_conta_contabil

ALTER TABLE pat_intangivel.tb_config_conta_contabil ADD CONSTRAINT pk_tb_config_conta_contabil PRIMARY KEY (ccc_id);

-- Table and Columns comments section

COMMENT ON TABLE pat_intangivel.tb_config_conta_contabil IS 'Dados de configura????o de contas cont??beis. ';

COMMENT ON COLUMN pat_intangivel.tb_config_conta_contabil.ccc_metodo IS 'M??todo utilizado para calcular a amortiza????o.';

COMMENT ON COLUMN pat_intangivel.tb_config_conta_contabil.ccc_tipo IS 'Tipo de opera????o de desvaloriza????o, pode ser deprecia????o, amortiza????o ou exaust??o.';

COMMENT ON COLUMN pat_intangivel.tb_config_conta_contabil.cc_id IS 'Chave estrangeira para conta cont??bil.';

COMMENT ON COLUMN pat_intangivel.tb_config_conta_contabil.ccc_dthr_cadastro IS 'Data/Hora de cria????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_config_conta_contabil.ccc_dthr_alteracao IS 'Data/Hora da ??ltima altera????o do registro.';

COMMENT ON COLUMN pat_intangivel.tb_config_conta_contabil.ccc_usuario_cadastro IS 'Usu??rio que criou o registro.';

COMMENT ON COLUMN pat_intangivel.tb_config_conta_contabil.ccc_usuario_alteracao IS '??ltimo usu??rio a alterar o registro.';


-- Grants (Oracle only)

GRANT SELECT, REFERENCES ON comum_siga.tb_conta_contabil TO pat_intangivel;
GRANT SELECT, REFERENCES ON comum_siga.tb_unidade_organizacional TO pat_intangivel;
GRANT SELECT, REFERENCES ON comum_siga.tb_tipo_documento TO pat_intangivel;
GRANT SELECT, REFERENCES ON comum_siga.tb_pessoa TO pat_intangivel;

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE pat_intangivel.tb_config_amortizacao ADD CONSTRAINT fk_contacontab_configdeprec FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (CC_ID);

ALTER TABLE pat_intangivel.tb_documento ADD CONSTRAINT fk_patrimonio_documento FOREIGN KEY (pa_id) REFERENCES pat_intangivel.tb_patrimonio (PA_ID);

ALTER TABLE pat_intangivel.tb_patrimonio ADD CONSTRAINT fk_unidadeorg_patrimonio FOREIGN KEY (uo_id_orgao) REFERENCES comum_siga.tb_unidade_organizacional (UO_ID);

ALTER TABLE pat_intangivel.tb_dados_amortizacao ADD CONSTRAINT fk_configdeprec_dadosamort FOREIGN KEY (ca_id) REFERENCES  pat_intangivel.tb_config_amortizacao (CD_ID);

ALTER TABLE pat_intangivel.tb_amortizacao ADD CONSTRAINT fk_patrimonio_amortizacao FOREIGN KEY (pa_id) REFERENCES pat_intangivel.tb_patrimonio (PA_ID);

ALTER TABLE pat_intangivel.tb_documento ADD CONSTRAINT fk_tipodoc_documento FOREIGN KEY (td_id) REFERENCES  comum_siga.tb_tipo_documento (TD_ID);

ALTER TABLE pat_intangivel.tb_patrimonio ADD CONSTRAINT fk_pessoa_patrimonio FOREIGN KEY (pe_id) REFERENCES comum_siga.tb_pessoa (PE_ID);

ALTER TABLE pat_intangivel.tb_dados_amortizacao ADD CONSTRAINT fk_patrimonio_dados_amort FOREIGN KEY (pa_id) REFERENCES  pat_intangivel.tb_patrimonio (PA_ID);

ALTER TABLE pat_intangivel.tb_patrimonio ADD CONSTRAINT fk_conta_patrimonio FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (CC_ID);

ALTER TABLE pat_intangivel.tb_amortizacao ADD CONSTRAINT fk_configdepre_amortizacao FOREIGN KEY (ca_id) REFERENCES pat_intangivel.tb_config_amortizacao (CD_ID);

ALTER TABLE pat_intangivel.tb_patrimonio ADD CONSTRAINT fk_dadosamort_patrimonio FOREIGN KEY (da_id) REFERENCES pat_intangivel.tb_dados_amortizacao (DA_ID);

ALTER TABLE pat_intangivel.tb_patrimonio ADD CONSTRAINT fk_uo_patrimonio_setor FOREIGN KEY (uo_id_setor) REFERENCES comum_siga.tb_unidade_organizacional (UO_ID);

ALTER TABLE pat_intangivel.tb_config_conta_contabil ADD CONSTRAINT fk_contacontab_configcontcont FOREIGN KEY (cc_id) REFERENCES comum_siga.tb_conta_contabil (CC_ID);
