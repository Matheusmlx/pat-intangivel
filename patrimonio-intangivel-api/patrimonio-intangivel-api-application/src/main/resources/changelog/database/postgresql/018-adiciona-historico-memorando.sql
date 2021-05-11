CREATE SEQUENCE "pat_intangivel"."seq_historico_memorando"
  INCREMENT BY 1
  START WITH 1
  NO MAXVALUE
  NO MINVALUE;

-- Table pat_intangivel.tb_historico_memorando

  CREATE TABLE "pat_intangivel"."tb_historico_memorando"
(
"hm_id" Integer NOT NULL,
"hm_numero_memorando" Character varying(11),
"hm_uri_anexo" Character varying(500) NOT NULL,
"hm_dthr_cadastro" Timestamp(9),
"hm_dthr_alteracao" Timestamp(9),
"hm_usuario_cadastro" Character varying(255),
"hm_usuario_alteracao" Character varying(255)
)
WITH (autovacuum_enabled=true);
