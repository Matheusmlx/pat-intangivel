-- Create sequences section -------------------------------------------------


CREATE SEQUENCE pat_intangivel.seq_patrimonio AS int
 START WITH 1
 INCREMENT BY 1
 NO MINVALUE
 NO MAXVALUE
go

CREATE SEQUENCE pat_intangivel.seq_config_depreciacao AS Int
 START WITH 1
 INCREMENT BY 1
 NO MINVALUE
 NO MAXVALUE
go

CREATE SEQUENCE pat_intangivel.seq_documento AS int
 START WITH 1
 INCREMENT BY 1
 NO MINVALUE
 NO MAXVALUE
go

CREATE SEQUENCE pat_intangivel.seq_amortizacao AS int
 START WITH 1
 INCREMENT BY 1
 NO MINVALUE
 NO MAXVALUE
go

CREATE SEQUENCE pat_intangivel.seq_dados_amortizacao AS int
  START WITH 1
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
go

CREATE SEQUENCE pat_intangivel.seq_config_contacontabil AS int
  START WITH 1
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
go


-- Create tables section -------------------------------------------------

-- Table pat_intangivel.TB_PATRIMONIO

CREATE TABLE pat_intangivel.TB_PATRIMONIO
(
 PA_ID Int NOT NULL,
 PA_TIPO Varchar(45) NOT NULL,
 PA_NOME Varchar(255) NULL,
 PA_NUMERO Varchar(20) NULL,
 PA_DESCRICAO Varchar(max) NULL,
 PA_SITUACAO Varchar(45) NULL,
 PA_ESTADO Varchar(45) NULL,
 PA_VALOR_LIQUIDO Decimal(20,6) NULL,
 PA_VALOR_AQUISICAO Decimal(20,6) NULL,
 PA_RECONHECIMENTO Varchar(45) NULL,
 PA_DTHR_AQUISICAO Datetime NULL,
 PA_DTHR_NL Datetime NULL,
 PA_NUMERO_NL Varchar(45) NULL,
 PA_DTHR_INICIO_VIDA_UTIL Datetime NULL,
 PA_DTHR_VENCIMENTO Datetime NULL,
 PA_MESES_VIDA_UTIL Smallint NULL,
 PA_DTHR_FIM_VIDA_UTIL Datetime NULL,
 PA_DTHR_ATIVACAO Datetime NULL,
 PA_VIDA_INDEFINIDA Bit default 0,
 PA_ATIVACAO_RETROATIVA Bit default 0,
 PA_AMORTIZAVEL Bit default 0,
 PA_DTHR_FINAL_ATIVACAO Varchar(255) NULL,
 UO_ID_ORGAO Int NULL,
 UO_ID_SETOR Int NULL,
 CC_ID Int NULL,
 PE_ID Int NULL,
 DA_ID Int NULL,
 PA_DTHR_CADASTRO Datetime NULL,
 PA_DTHR_ALTERACAO Datetime NULL,
 PA_USUARIO_CADASTRO Varchar(255) NULL,
 PA_USUARIO_ALTERACAO Varchar(255) NULL
)
go


EXEC sp_addextendedproperty 'MS_Description', 'Cadastro dos patrimônios intangíveis.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Tipo de bem intangível. Pode assumir os valores: SOFTWARE, DIREITOS_AUTORAIS, LICENCAS, MARCAS, TITULOS_DE_PUBLICACAO, RECEITAS_FORMULAS_PROJETOS.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_TIPO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Nome do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_NOME'
go
EXEC sp_addextendedproperty 'MS_Description', 'Numero do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_NUMERO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Descricao do patrimonio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DESCRICAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Situação do patrimônio. Pode assumir os valores: EM_ELABORACAO, ATIVO e BAIXADO.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_SITUACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Estado do bem intangível. Pode assumir os valores: PRONTO_PARA_USO e EM_DESENVOLVIMENTO.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_ESTADO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor liquido atual do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_VALOR_LIQUIDO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor de aquisição final do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_VALOR_AQUISICAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Ação pela qual é definido como o intangível foi adquirido. Pode assumir os valores: AQUISICAO_SEPARADA, GERACAO_INTERNA e TRANSACAO_SEM_CONTRAPRESTACAO.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_RECONHECIMENTO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data de aquisição do bem.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DTHR_AQUISICAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data da nota de lançamento.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DTHR_NL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Número da nota de lançamento do bem.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_NUMERO_NL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data de início da vida útil do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DTHR_INICIO_VIDA_UTIL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data de vencimento na qual o bem intangivel deixa de ter valor, ou a licença de um software expira.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DTHR_VENCIMENTO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Quantidade de meses de vida útil do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_MESES_VIDA_UTIL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data em que se encerra a vida útil do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DTHR_FIM_VIDA_UTIL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor booleano para indicar se patrimônio possui vida útil indefinida.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_VIDA_INDEFINIDA'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor booleano para indicar se patrimônio possui ativação retroativa.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_ATIVACAO_RETROATIVA'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor booleano para indicar se patrimônio é amortizável.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_AMORTIZAVEL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de ativação do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DTHR_FINAL_ATIVACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o orgão a qual o patrimônio pertence.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'UO_ID_ORGAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o setor a qual o patrimônio pertence.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'UO_ID_SETOR'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a conta contábil do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'CC_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o fornecedor do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PE_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a configuracao de amortização do patrimônio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'DA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_PATRIMONIO', 'COLUMN', 'PA_USUARIO_ALTERACAO'
go

-- Create indexes for table pat_intangivel.TB_PATRIMONIO

CREATE INDEX IN_PA_UO_ID_ORGAO ON pat_intangivel.TB_PATRIMONIO (UO_ID_ORGAO)
go

CREATE INDEX IN_PA_PE_ID ON pat_intangivel.TB_PATRIMONIO (PE_ID)
go

CREATE INDEX IN_PA_CC_ID ON pat_intangivel.TB_PATRIMONIO (CC_ID)
go

CREATE INDEX IN_PA_DA_ID ON pat_intangivel.TB_PATRIMONIO (DA_ID)
go

CREATE INDEX IN_PA_UO_ID_SETOR ON pat_intangivel.TB_PATRIMONIO (UO_ID_SETOR)
go

-- Add keys for table pat_intangivel.TB_PATRIMONIO

ALTER TABLE pat_intangivel.TB_PATRIMONIO ADD CONSTRAINT PK_TB_PATRIMONIO PRIMARY KEY (PA_ID)
go

-- Table pat_intangivel.TB_CONFIG_AMORTIZACAO

CREATE TABLE pat_intangivel.TB_CONFIG_AMORTIZACAO
(
 CA_ID Int NOT NULL,
 CA_METODO Varchar(45) NULL,
 CA_VIDA_UTIL_MESES Smallint NULL,
 CA_SITUACAO Varchar(45) NOT NULL,
 CA_TAXA Decimal(12,6) NULL,
 CA_PERCENTUAL_RESIDUAL Decimal(12,6) NULL,
 CA_TIPO Varchar(45) NOT NULL,
 CC_ID Int NOT NULL,
 CA_DTHR_CADASTRO Datetime NULL,
 CA_DTHR_ALTERACAO Datetime NULL,
 CA_USUARIO_CADASTRO Varchar(255) NULL,
 CA_USUARIO_ALTERACAO Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Dados para cálculo de amortizações (intangíveis/benfeitorias) e depreciações (ativos imobilizados). ', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Método utilizado para calcular a amortização/depreciação.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'COLUMN', 'CA_METODO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Vida util do bem em meses.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'COLUMN', 'CA_VIDA_UTIL_MESES'
go
EXEC sp_addextendedproperty 'MS_Description', 'Situação dos dados de amortização/depreciação da conta contábil. Pode assumir os valores:', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'COLUMN', 'CA_SITUACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Taxa de depreciação/amortização para conta contábil.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'COLUMN', 'CA_TAXA'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor percentual que o patrimônio terá após o fim da vida útil.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'COLUMN', 'CA_PERCENTUAL_RESIDUAL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Tipo de operação de desvalorização, pode ser depreciação, amortização ou exaustão.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'COLUMN', 'CA_TIPO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para conta contábil.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'COLUMN', 'CC_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'column', 'CA_DTHR_CADASTRO';
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'column', 'CA_DTHR_ALTERACAO';
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'column', 'CA_USUARIO_CADASTRO';
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_AMORTIZACAO', 'column', 'CA_USUARIO_ALTERACAO';
go

-- Create indexes for table pat_intangivel.TB_CONFIG_AMORTIZACAO

CREATE INDEX IN_CA_CC_ID ON pat_intangivel.TB_CONFIG_AMORTIZACAO ([CC_ID])
go

-- Add keys for table pat_intangivel.TB_CONFIG_AMORTIZACAO

ALTER TABLE pat_intangivel.TB_CONFIG_AMORTIZACAO ADD CONSTRAINT PK_TB_CONFIG_DEPRECIACAO PRIMARY KEY (CA_ID)
go

-- Table pat_intangivel.TB_DOCUMENTO

CREATE TABLE pat_intangivel.TB_DOCUMENTO
(
 DO_ID Int NOT NULL,
 DO_NUMERO Varchar(45) NOT NULL,
 DO_DT Datetime,
 DO_VALOR Decimal(20,6),
 DO_URI_ANEXO Varchar(500) NOT NULL,
 PA_ID Int NOT NULL,
 TD_ID Int NOT NULL,
 DO_DTHR_CADASTRO Datetime NULL,
 DO_DTHR_ALTERACAO Datetime NULL,
 DO_USUARIO_CADASTRO Varchar(255) NULL,
 DO_USUARIO_ALTERACAO Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Documentos referentes ao bem.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Número de identificação do documento.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'DO_NUMERO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data do documento.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'DO_DT'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor do dcocumento.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'DO_VALOR'
go
EXEC sp_addextendedproperty 'MS_Description', 'Caminho para o documento anexado.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'DO_URI_ANEXO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o patrimonio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'PA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o tipo de documento.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'TD_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'DO_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'DO_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'DO_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'DO_USUARIO_ALTERACAO'
go

-- Create indexes for table pat_intangivel.TB_DOCUMENTO

CREATE INDEX IN_DO_PA_ID ON pat_intangivel.TB_DOCUMENTO (PA_ID)
go

CREATE INDEX IN_DO_TD_ID ON pat_intangivel.TB_DOCUMENTO (TD_ID)
go

-- Add keys for table pat_intangivel.TB_DOCUMENTO

ALTER TABLE pat_intangivel.TB_DOCUMENTO ADD CONSTRAINT PK_TB_DOCUMENTO PRIMARY KEY (DO_ID)
go

-- Table pat_intangivel.TB_HISTORICO_DEPRECIACAO

CREATE TABLE pat_intangivel.TB_AMORTIZACAO
(
 AM_ID Int NOT NULL,
 AM_DTHR_INICIAL Datetime NOT NULL,
 AM_DTHR_FINAL Datetime NOT NULL,
 AM_VALOR_ANTERIOR Decimal(20,6) NOT NULL,
 AM_VALOR_POSTERIOR Decimal(20,6) NOT NULL,
 AM_VALOR_SUBTRAIDO Decimal(20,6) NOT NULL,
 AM_TAXA_APLICADA Decimal(20,6) NOT NULL,
 PA_ID Int NOT NULL,
 CA_ID Int NOT NULL,
 AM_DTHR_CADASTRO Datetime NULL,
 AM_DTHR_ALTERACAO Datetime NULL,
 AM_USUARIO_CADASTRO Varchar(255) NULL,
 AM_USUARIO_ALTERACAO Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Amortizações realizadas nos Patrimônios.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave primária.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data no mês a partir da qual o bem começou a amortização.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_DTHR_INICIAL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data final em determinado mês usada para calcular a amortização mensal do bem.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_DTHR_FINAL'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor liquido que o bem possui antes da amortização mensal.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_VALOR_ANTERIOR'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor liquido do bem após a amortização mensal.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_VALOR_POSTERIOR'
go
EXEC sp_addextendedproperty 'MS_Description', 'Valor mensal amortizado no bem.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_VALOR_SUBTRAIDO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Taxa de amortização refrente ao período aplicado.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_TAXA_APLICADA'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o patrimonio que amortizou.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'PA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para o conta contabil que amortizou.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'CA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_AMORTIZACAO', 'COLUMN', 'AM_USUARIO_ALTERACAO'
go

-- Create indexes for table pat_intangivel.TB_AMORTIZACAO

CREATE INDEX IN_AM_PA_ID ON pat_intangivel.TB_AMORTIZACAO (PA_ID)
go

CREATE INDEX IN_AM_CA_ID ON pat_intangivel.TB_AMORTIZACAO (CA_ID)
go

-- Add keys for table pat_intangivel.TB_AMORTIZACAO

ALTER TABLE pat_intangivel.TB_AMORTIZACAO ADD CONSTRAINT PK_TB_AMORTIZACAO PRIMARY KEY (AM_ID)
go

-- Table pat_intangivel.TB_DADOS_AMORTIZACAO

CREATE TABLE pat_intangivel.TB_DADOS_AMORTIZACAO
(
 DA_ID Int NOT NULL,
 CA_ID Int NOT NULL,
 PA_ID Int NOT NULL,
 DA_DTHR_CADASTRO Datetime NULL,
 DA_DTHR_ALTERACAO Datetime NULL,
 DA_USUARIO_CADASTRO Varchar(255) NULL,
 DA_USUARIO_ALTERACAO Varchar(255) NULL
)
go

EXEC sp_addextendedproperty 'MS_Description', 'Relação entre o Patrimônio e os Dados de Amortização.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DADOS_AMORTIZACAO', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a conta contábil.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DADOS_AMORTIZACAO', 'COLUMN', 'CA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a patrimonio.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DADOS_AMORTIZACAO', 'COLUMN', 'PA_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DADOS_AMORTIZACAO', 'COLUMN', 'DA_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DADOS_AMORTIZACAO', 'COLUMN', 'DA_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DADOS_AMORTIZACAO', 'COLUMN', 'DA_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_DADOS_AMORTIZACAO', 'COLUMN', 'DA_USUARIO_ALTERACAO'
go

-- Create indexes for table pat_intangivel.TB_DADOS_AMORTIZACAO

CREATE INDEX IN_DA_CA_ID ON pat_intangivel.TB_DADOS_AMORTIZACAO (CA_ID)
go

CREATE INDEX IN_DA_PA_ID ON pat_intangivel.TB_DADOS_AMORTIZACAO (PA_ID)
go

-- Add keys for table pat_intangivel.TB_DADOS_AMORTIZACAO

ALTER TABLE pat_intangivel.TB_DADOS_AMORTIZACAO ADD CONSTRAINT PK_TB_DADOS_AMORTIZACAO PRIMARY KEY (DA_ID)
go


-- Table pat_intangivel.tb_config_conta_contabil

CREATE TABLE pat_intangivel.TB_CONFIG_CONTA_CONTABIL
(
  CCC_ID Integer NOT NULL,
  CCC_METODO Varchar(45) NULL,
  CCC_TIPO Varchar(45) NOT NULL,
  CC_ID Integer NOT NULL,
  CCC_DTHR_CADASTRO Datetime NULL,
  CCC_DTHR_ALTERACAO Datetime NULL,
  CCC_USUARIO_CADASTRO Varchar(255) NULL,
  CCC_USUARIO_ALTERACAO Varchar(255) NULL
)
go

-- Table and Columns comments section

EXEC sp_addextendedproperty 'MS_Description', 'Dados de configuração de contas contábeis. ', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', NULL, NULL
go
EXEC sp_addextendedproperty 'MS_Description', 'Método utilizado para calcular a amortização.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'CCC_METODO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Tipo de operação de desvalorização, pode ser depreciação, amortização ou exaustão.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'CCC_TIPO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para conta contábil.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'CC_ID'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora de criação do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'CCC_DTHR_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Data/Hora da última alteração do registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'CCC_DTHR_ALTERACAO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Usuário que criou o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'CCC_USUARIO_CADASTRO'
go
EXEC sp_addextendedproperty 'MS_Description', 'Último usuário a alterar o registro.', 'SCHEMA', 'pat_intangivel', 'TABLE', 'TB_CONFIG_CONTA_CONTABIL', 'COLUMN', 'CCC_USUARIO_ALTERACAO'
go


-- Create indexes for table pat_intangivel.TB_CONFIG_CONTA_CONTABIL

CREATE INDEX IN_CCC_CC_ID ON pat_intangivel.TB_CONFIG_CONTA_CONTABIL (cc_id)
go

-- Add keys for table pat_intangivel.TB_CONFIG_CONTA_CONTABIL

ALTER TABLE pat_intangivel.TB_CONFIG_CONTA_CONTABIL ADD CONSTRAINT PK_TB_CONFIG_CONTA_CONTABIL PRIMARY KEY (ccc_id)
go


-- Create foreign keys (relationships) section -------------------------------------------------


ALTER TABLE pat_intangivel.TB_CONFIG_AMORTIZACAO ADD CONSTRAINT FK_CONTACONTAB_CONFIGDEPREC FOREIGN KEY (CC_ID) REFERENCES comum_siga.TB_CONTA_CONTABIL (CC_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_DOCUMENTO ADD CONSTRAINT FK_PATRIMONIO_DOCUMENTO FOREIGN KEY (PA_ID) REFERENCES pat_intangivel.TB_PATRIMONIO (PA_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_PATRIMONIO ADD CONSTRAINT FK_UNIDADEORG_PATRIMONIO FOREIGN KEY (UO_ID_ORGAO) REFERENCES comum_siga.TB_UNIDADE_ORGANIZACIONAL (UO_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_DADOS_AMORTIZACAO ADD CONSTRAINT FK_CONFIGDEPREC_DADOSAMORT FOREIGN KEY (CA_ID) REFERENCES pat_intangivel.TB_CONFIG_AMORTIZACAO (CA_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_AMORTIZACAO ADD CONSTRAINT FK_PATRIMONIO_AMORTIZACAO FOREIGN KEY (PA_ID) REFERENCES pat_intangivel.TB_PATRIMONIO (PA_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_DOCUMENTO ADD CONSTRAINT FK_TIPODOC_DOCUMENTO FOREIGN KEY (TD_ID) REFERENCES comum_siga.TB_TIPO_DOCUMENTO (TD_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_PATRIMONIO ADD CONSTRAINT FK_PESSOA_PATRIMONIO FOREIGN KEY (PE_ID) REFERENCES comum_siga.TB_PESSOA (PE_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_DADOS_AMORTIZACAO ADD CONSTRAINT FK_PATRIMONIO_DADOSAMORT FOREIGN KEY (PA_ID) REFERENCES pat_intangivel.TB_PATRIMONIO (PA_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_PATRIMONIO ADD CONSTRAINT FK_CONTA_PATRIMONIO FOREIGN KEY (CC_ID) REFERENCES comum_siga.TB_CONTA_CONTABIL (CC_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_AMORTIZACAO ADD CONSTRAINT FK_CONFIGDEPRE_AMORTIZACAO FOREIGN KEY (CA_ID) REFERENCES pat_intangivel.TB_CONFIG_AMORTIZACAO (CA_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_PATRIMONIO ADD CONSTRAINT FK_DADOSAMORT_PATRIMONIO FOREIGN KEY (DA_ID) REFERENCES pat_intangivel.TB_DADOS_AMORTIZACAO (DA_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_PATRIMONIO ADD CONSTRAINT FK_UO_PATRIMONIO_SETOR FOREIGN KEY (UO_ID_SETOR) REFERENCES comum_siga.TB_UNIDADE_ORGANIZACIONAL (UO_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go

ALTER TABLE pat_intangivel.TB_CONFIG_CONTA_CONTABIL ADD CONSTRAINT FK_CONTACONTAB_CONFIGCONTCONT FOREIGN KEY (CC_ID) REFERENCES comum_siga.TB_CONTA_CONTABIL (CC_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go
