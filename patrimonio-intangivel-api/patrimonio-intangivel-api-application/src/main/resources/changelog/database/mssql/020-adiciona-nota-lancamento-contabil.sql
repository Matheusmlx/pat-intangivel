-- Create sequences section--------------------------------------------------------------------------------

CREATE SEQUENCE pat_intangivel.seq_nota_lancto_contabil AS int
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
go

-- Table pat_intangivel.tb_nota_lancto_contabil

CREATE TABLE pat_intangivel.TB_NOTA_LANCTO_CONTABIL
(
    NLC_ID INT NOT  NULL,
    NLC_NUMERO Varchar (45),
    NLC_DTHR_LANCAMENTO Datetime,
    NLC_DTHR_CADASTRO Datetime (9),
    NLC_DTHR_ALTERACAO Datetime (9),
    NLC_USUARIO_CADASTRO Varchar(255),
    NLC_USUARIO_ALTERACAO Varchar(255)
)
go

EXEC sp_addextendedproperty 'MS_Description', 'armazena as informações de um registro de nota de lançamento contábil.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_NOTA_LANCTO_CONTABIL', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave primária da tabela nota de lançamento', 'SCHEMA', 'pat_intangivel', 'TABLE', 'tb_nota_lancto_contabil', 'COLUMN', 'NLC_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'armazena o número da nota de lançamento contábil', 'SCHEMA', 'pat_intangivel', 'TABLE', 'tb_nota_lancto_contabil', 'COLUMN', 'NLC_NUMERO'
go
EXEC sp_addextendedproperty 'MS_Description', 'data/hora em que foi realizado o lançamento contábil', 'SCHEMA', 'pat_intangivel', 'TABLE', 'tb_nota_lancto_contabil', 'COLUMN', 'NLC_DTHR_LANCAMENTO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo de auditoria que guarda a data de cadastro', 'SCHEMA', 'pat_intangivel', 'TABLE', 'tb_nota_lancto_contabil', 'COLUMN', 'NLC_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo de auditoria que guarda a data de edição', 'SCHEMA', 'pat_intangivel', 'TABLE', 'tb_nota_lancto_contabil', 'COLUMN', 'NLC_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo de auditoria que guarda o usuário que realizou o cadastro', 'SCHEMA', 'pat_intangivel', 'TABLE', 'tb_nota_lancto_contabil', 'COLUMN', 'NLC_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Campo de auditoria que guarda o usuário que realizou a edição', 'SCHEMA', 'pat_intangivel', 'TABLE', 'tb_nota_lancto_contabil', 'COLUMN', 'NLC_USUARIO_ALTERACAO'
go

ALTER TABLE pat_intangivel.TB_NOTA_LANCTO_CONTABIL ADD CONSTRAINT PK_TB_NOTA_LANCTO_CONTABIL PRIMARY KEY (NLC_ID)
go

ALTER TABLE pat_intangivel.TB_PATRIMONIO ADD COLUMN  NLC_ID INT;

EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a tabela Nota de Lançamento Contabil', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'NLC_ID'
go

--UPDATE QUERY IN TABLE "PAT_INTANGIVEL"."TB_PATRIMONIO"
ALTER TABLE pat_intangivel.TB_PATRIMONIO ADD NLC_ID Int;

EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a tabela Nota de Lançamento Contábil', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'NLC_ID'
go

CREATE INDEX IN_IN_NLC_ID ON pat_intangivel.TB_PATRIMONIO (NLC_ID)
go

--UPDATE QUERY IN TABLE "PAT_INTANGIVEL"."TB_MOVIMENTACAO"
ALTER TABLE pat_intangivel.TB_MOVIMENTACAO ADD NLC_ID Int;

EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a tabela Nota de Lançamento Contábil', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_MOVIMENTACAO', 'COLUMN', 'NLC_ID'
go

CREATE INDEX IN_IN_NLC_ID ON pat_intangivel.TB_MOVIMENTACAO (NLC_ID)
go
