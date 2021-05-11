CREATE SEQUENCE pat_intangivel.seq_movimentacao AS int
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
go

CREATE SEQUENCE PAT_INTANGIVEL.SEQ_LANCAMENTOS_CONTABEIS AS int
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
go

-- Update tables section -------------------------------------------------

-- Table pat_intangivel.tb_patrimonio

ALTER TABLE pat_intangivel.TB_PATRIMONIO
    ADD  PA_VALOR_ENTRADA Decimal(20,6);
go

EXEC sp_addextendedproperty 'MS_Description', 'Valor de entrada do patrimônio no órgão atual.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_VALOR_ENTRADA'
go

-- Table pat_intangivel.tb_amortizacao

ALTER TABLE pat_intangivel.TB_AMORTIZACAO
    ADD UO_ID_ORGAO Int;
go

ALTER TABLE pat_intangivel.TB_AMORTIZACAO ADD CONSTRAINT FK_UNIDADEORG_AMORTIZACAO FOREIGN KEY (UO_ID_ORGAO) REFERENCES comum_siga.TB_UNIDADE_ORGANIZACIONAL (UO_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

CREATE INDEX IN_PA_AMOR_UO_ID_ORGAO ON pat_intangivel.TB_AMORTIZACAO (UO_ID_ORGAO)
go

EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o orgão a qual a amortizacao foi executada.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'UO_ID_ORGAO'
go

-- Create tables section -------------------------------------------------

-- Table pat_intangivel.TB_MOVIMENTACAO
CREATE TABLE pat_intangivel.TB_MOVIMENTACAO
(
    MO_ID Int NOT NULL,
    MO_CODIGO Varchar(45),
    MO_TIPO Varchar (45) NOT NULL,
    UO_ID_ORGAO_ORIGEM Int,
    UO_ID_ORGAO_DESTINO Int,
    PA_ID Int NOT NULL,
    MO_SITUACAO Varchar (45),
    MO_DTHR_NL Datetime,
    MO_NUMERO_NL Varchar (45),
    MO_DTHR_FINALIZACAO Datetime,
    MO_USUARIO_FINALIZACAO Varchar (255),
    MO_DTHR_ENVIO Datetime,
    MO_MOTIVO_OBS Varchar (500),
    MO_MOTIVO_REJEICAO Varchar (500),
    MO_DTHR_CADASTRO Datetime,
    MO_DTHR_ALTERACAO Datetime,
    MO_USUARIO_CADASTRO Varchar (255),
    MO_USUARIO_ALTERACAO Varchar(255),
    MO_NUMERO_MEMORANDO Varchar(6),
    MO_ANO_MEMORANDO Varchar(6)
)
go

EXEC  'MS_Description', 'Cadastro das movimentações.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Código da movimentação.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_CODIGO'
go
EXEC sp_addextendedproperty 'MS_Description', 'É o tipo de movimentação. Pode assumir os valores: TRANSFERENIA_DEFINITIVA, TRANSFERENCIA_TEMPORARIA, DOACAO_ENTRE_ORGAOS e DOACAO_PARA_TERCEIROS', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_TIPO'
go
EXEC sp_addextendedproperty 'MS_Description', 'É o id do Orgão de Origem.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'UO_ID_ORGAO_ORIGEM'
go
EXEC sp_addextendedproperty 'MS_Description', 'É o id do Orgão de destino.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'UO_ID_ORGAO_DESTINO'
go
EXEC sp_addextendedproperty 'MS_Description', 'É o id do Patrimônio que a movimentação pertence', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'PA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'São as situações que a movimentação pode se encontrar. Pode assumir os valores: EM_ELABORACAO, AGUARDANDO_RECEBIMENTO e FINALIZADO,REJEITADO', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_SITUACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'É a data de NL da movimentação', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_DTHR_NL'
go
EXEC sp_addextendedproperty 'MS_Description', 'É numero de NL da movimentação', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_NUMERO_NL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que finalizou a movimentação', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_USUARIO_FINALIZACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'É a data de finalizacao da movimentação podendo o mesmo ter sido finalizado ou rejeitado', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_DTHR_FINALIZACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'É a data de envio da movimentação', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_DTHR_ENVIO'
go
EXEC sp_addextendedproperty 'MS_Description', 'É o motivo pela aquela movimentação está sendo registrada', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_MOTIVO_OBS'
go
EXEC sp_addextendedproperty 'MS_Description', 'É o motivo pela aquela movimentação foi rejeitada', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_MOTIVO_REJEICAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'É o número do memorando', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_NUMERO_MEMORANDO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Ano do memorando', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_ANO_MEMORANDO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'MO_USUARIO_ALTERACAO'
go

-- CREATE INDEXES FOR TABLE pat_intangivel.TB_MOVIMENTACAO

CREATE INDEX IN_PA_UO_ID_ORGAO_ORIGEM ON pat_intangivel.TB_MOVIMENTACAO (UO_ID_ORGAO_ORIGEM)
go

CREATE INDEX IN_PA_UO_ID_ORGAO_DESTINO ON pat_intangivel.TB_MOVIMENTACAO (UO_ID_ORGAO_DESTINO)
go

CREATE INDEX IN_PA_PA_ID ON pat_intangivel.TB_MOVIMENTACAO (PA_ID)
go

-- ADD KEYS FOR TABLE pat_intangivel.TB_MOVIMENTACAO

ALTER TABLE pat_intangivel.TB_MOVIMENTACAO ADD CONSTRAINT PK_TB_MOVIMENTACAO PRIMARY KEY (MO_ID)
go

-- TABLE PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS

CREATE TABLE PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS
(
    LC_ID Int NOT NULL,
    LC_DATA Datetime NOT NULL,
    LC_TIPO_LANCAMENTO Varchar (45) NOT NULL,
    LC_MOTIVO_LANCAMENTO Varchar (45) NOT NULL,
    LC_VALOR_LIQUIDO Decimal(20,6) NULL,
    PA_ID Int,
    MO_ID Int,
    UO_ID_ORGAO Int NULL,
    CC_ID Int,
    LC_DTHR_CADASTRO Datetime NULL,
    LC_DTHR_ALTERACAO Datetime NULL,
    LC_USUARIO_CADASTRO Varchar(255) NULL,
    LC_USUARIO_ALTERACAO Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Cadastro dos lançamentos contábeis.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o patrimonio.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'PA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o orgão a qual o lançamento contabíl pertence.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'MO_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o orgão a qual o lançamento contabíl pertence.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'UO_ID_ORGAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a conta contábil do lançamento contabíl.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'CC_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Tipo do lançamento contabíl. Pode assumir os valores: CREDITO e DEBITO.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'LC_TIPO_LANCAMENTO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Motivo do lançamento contabíl. Pode assumir os valores: CANCELAMENTO_ATIVACAO, ATIVACAO, TRANSFERENCIA_DEFINITIVA e DOACAO_ORGAOS', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'LC_MOTIVO_LANCAMENTO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor líquido do patrimônio no momento do lançamento contabíl', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'LC_VALOR_LIQUIDO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'LC_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'LC_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'LC_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_LANCAMENTOS_CONTABEIS', 'COLUMN', 'LC_USUARIO_ALTERACAO'
go

-- CREATE INDEXES FOR TABLE PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS

CREATE INDEX IN_LC_UO_ID_ORGAO ON PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS (UO_ID_ORGAO)
go

CREATE INDEX IN_LC_CC_ID ON PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS (CC_ID)
go

CREATE INDEX IN_LC_PA_ID ON PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS (PA_ID)
go

CREATE INDEX IN_LC_MO_ID ON PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS (MO_ID)
go

-- ADD KEYS FOR TABLE PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS

ALTER TABLE PAT_INTANGIVEL.TB_LANCAMENTOS_CONTABEIS ADD CONSTRAINT PK_TB_LANCAMENTOS_CONTABEIS PRIMARY KEY (LC_ID)
go

-- ADD COMMENTS FOR TABLE PAT_INTANGIVEL.TB_HISTORICO_MEMORANDO

EXEC sp_addextendedproperty 'MS_Description', 'Cadastro dos hitóricos dos memorandos.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_HISTORICO_MEMORANDO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Tipo do lançamento contabíl. Pode assumir os valores: CREDITO e DEBITO.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_HISTORICO_MEMORANDO', 'COLUMN', 'hm_numero_memorando'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número do memorando gerado.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_HISTORICO_MEMORANDO', 'COLUMN', 'hm_uri_anexo'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_HISTORICO_MEMORANDO', 'COLUMN', 'hm_dthr_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_HISTORICO_MEMORANDO', 'COLUMN', 'hm_dthr_alteracao'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_HISTORICO_MEMORANDO', 'COLUMN', 'hm_usuario_cadastro'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'PAT_INTANGIVEL', 'TABLE', 'TB_HISTORICO_MEMORANDO', 'COLUMN', 'hm_usuario_alteracao'
go

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE pat_intangivel.TB_LANCAMENTOS_CONTABEIS ADD CONSTRAINT FK_PA_ID_LANCAMENTOS_CONTABEIS FOREIGN KEY (PA_ID) REFERENCES PAT_INTANGIVEL.TB_PATRIMONIO (PA_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_LANCAMENTOS_CONTABEIS ADD CONSTRAINT FK_MO_ID_LANCAMENTOS_CONTABEIS FOREIGN KEY (MO_ID) REFERENCES PAT_INTANGIVEL.TB_MOVIMENTACAO (MO_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_LANCAMENTOS_CONTABEIS ADD CONSTRAINT FK_CONTA_LANCAMENTOS_CONTABEIS FOREIGN KEY (CC_ID) REFERENCES comum_siga.TB_CONTA_CONTABIL (CC_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_LANCAMENTOS_CONTABEIS ADD CONSTRAINT FK_UNIDADEORG_LANCAMENTOS_CONTABEIS FOREIGN KEY (UO_ID_ORGAO) REFERENCES comum_siga.TB_UNIDADE_ORGANIZACIONAL (UO_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_MOVIMENTACAO ADD CONSTRAINT FK_PATRIMONIO_MOVIMENTACAO FOREIGN KEY (PA_ID) REFERENCES pat_intangivel.TB_PATRIMONIO (PA_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_MOVIMENTACAO ADD CONSTRAINT FK_UNIDADEORG_ORIGEM_MOVIMENTACAO FOREIGN KEY (UO_ID_ORGAO_ORIGEM) REFERENCES comum_siga.TB_UNIDADE_ORGANIZACIONAL (UO_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_MOVIMENTACAO ADD CONSTRAINT FK_UNIDADEORG_DESTINO_MOVIMENTACAO FOREIGN KEY (UO_ID_ORGAO_DESTINO) REFERENCES comum_siga.TB_UNIDADE_ORGANIZACIONAL (UO_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE PAT_INTANGIVEL.TB_HISTORICO_MEMORANDO ADD CONSTRAINT PK_TB_HISTORICO_MEMORANDO PRIMARY KEY (HM_ID)
go


-- Update Views section -------------------------------------------------

IF OBJECT_ID('pat_intangivel.VI_RELATORIO_ANALITICO', 'V') IS NOT NULL
    DROP VIEW [pat_intangivel].[VI_RELATORIO_ANALITICO]
go

IF OBJECT_ID('pat_intangivel.VI_RELATORIO_SINTETICO', 'V') IS NOT NULL
    DROP VIEW [pat_intangivel].[VI_RELATORIO_SINTETICO]
go

CREATE VIEW pat_intangivel.VI_RELATORIO_SINTETICO AS
SELECT pa.pa_id              AS RS_PATRIMONIO,
       pa.uo_id_orgao        AS RS_ORGAO,
       pa.pa_valor_aquisicao AS RS_VALOR_AQUISICAO,
       pa.pa_dthr_ativacao   AS RS_PA_ATIVACAO,
       cc.cc_id              AS RS_CONTA_CONTABIL_ID,
       am.am_valor_subtraido AS RS_VALOR_SUBTRA_AMORT,
       am.am_valor_posterior AS RS_VALOR_LIQUIDO,
       am.am_dthr_final      AS RS_DATA_AMORT,
       am.uo_id_orgao        AS RS_UO_ID_ORGAO_AMORT,
       row_number() OVER (ORDER BY (SELECT NULL)) AS RS_ID
FROM PAT_INTANGIVEL.TB_PATRIMONIO pa
         LEFT JOIN COMUM_SIGA.TB_CONTA_CONTABIL cc  ON pa.cc_id = cc.CC_ID
         LEFT JOIN PAT_INTANGIVEL.TB_AMORTIZACAO am ON pa.pa_id = am.PA_ID
WHERE pa.PA_SITUACAO = 'ATIVO';
go
