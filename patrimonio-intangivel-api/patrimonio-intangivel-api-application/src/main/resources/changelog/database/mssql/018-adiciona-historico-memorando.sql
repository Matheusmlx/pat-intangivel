CREATE SEQUENCE pat_intangivel.seq_historico_memorando AS int
  START WITH 1
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
go

-- Table pat_intangivel.TB_HISTORICO_MEMORANDO

CREATE TABLE pat_intangivel.TB_HISTORICO_MEMORANDO
(
hm_id Int not null,
hm_numero_memorando varchar(11),
hm_uri_anexo Character varying(500) NOT NULL,
hm_dthr_cadastro Datetime NULL,
hm_dthr_alteracao Datetime NULL,
hm_usuario_cadastro Varchar(255) NULL,
hm_usuario_alteracao Varchar(255) NULL
)
go
