  CREATE SEQUENCE "pat_intangivel"."seq_movimentacao"
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE ;

  CREATE SEQUENCE "pat_intangivel"."seq_lancamentos_contabeis"
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE ;

  -- Update tables section -------------------------------------------------

  -- Table pat_intangivel.tb_patrimonio

  ALTER TABLE "pat_intangivel"."tb_patrimonio"
      ADD COLUMN "pa_valor_entrada" Numeric(20,6);

  comment on column "pat_intangivel"."tb_patrimonio"."pa_valor_entrada" is 'Valor de entrada do patrimônio no órgão atual.';

  -- Table pat_intangivel.tb_amortizacao

  ALTER TABLE "pat_intangivel"."tb_amortizacao"
      ADD COLUMN "uo_id_orgao" integer;

  create index in_pa_amor_uo_id_orgao on "pat_intangivel"."tb_amortizacao" (uo_id_orgao);

  ALTER TABLE "pat_intangivel"."tb_amortizacao"  ADD CONSTRAINT "fk_unidadeorg_amortizacao" FOREIGN KEY ("uo_id_orgao") REFERENCES "comum_siga"."tb_unidade_organizacional" ("uo_id") ON DELETE NO ACTION ON UPDATE NO ACTION;

  comment on column "pat_intangivel"."tb_amortizacao"."uo_id_orgao" is 'Chave estrangeira para o orgão a qual a amortização foi executada.';

  -- Create tables section -------------------------------------------------

  -- Table pat_intangivel.tb_movimentacao

  CREATE TABLE "pat_intangivel"."tb_movimentacao"
(
    "mo_id" Integer NOT NULL,
    "mo_codigo" Character varying(20),
    "mo_tipo" Character varying (45) NOT NULL,
    "uo_id_orgao_origem" Integer,
    "uo_id_orgao_destino" Integer,
    "pa_id" Integer NOT NULL,
    "mo_situacao" Character varying (45),
    "mo_dthr_nl" timestamp (9),
    "mo_numero_nl" Character varying (45),
    "mo_dthr_envio" timestamp (9),
    "mo_motivo_obs" Character varying (500),
    "mo_motivo_rejeicao" Character varying (500),
    "mo_usuario_finalizacao" character varying(255),
    "mo_dthr_finalizacao" timestamp (9),
    "mo_dthr_cadastro" timestamp(9),
    "mo_dthr_alteracao" timestamp(9),
    "mo_usuario_cadastro" Character varying (255),
    "mo_usuario_alteracao" character varying(255),
    "mo_numero_memorando" character varying (6),
    "mo_ano_memorando" character varying (4)
)

WITH (autovacuum_enabled=true);

COMMENT ON TABLE "pat_intangivel"."tb_movimentacao" IS 'Dados de configuração de contas contábeis.';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_codigo" IS 'Código da movimentação.';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_tipo" IS 'É o tipo de movimentação. Pode assumir os valores:TRANSFERENIA_DEFINITIVA,TRANSFERENCIA_TEMPORARIA,DOACAO_ENTRE_ORGAOS,DOACAO_PARA_TERCEIROS';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."uo_id_orgao_origem" IS 'É o id do Orgão de Origem';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."uo_id_orgao_destino" IS 'É o id do Orgão de destino';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."pa_id" IS 'É o id do Patrimônio que a movimentação pertence';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_usuario_cadastro" IS 'É o usuario que cadastro essa movimentação';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_situacao" IS 'São as situações que a movimentação pode se encontrar. Pode assumir os valores:EM_ELABORACAO,AGUARDANDO_RECEBIMENTO,FINALIZADO,REJEITADO';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_dthr_nl" IS 'É a data de NL da movimentação';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_numero_nl" IS 'É numero de NL da movimentação';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_dthr_finalizacao" IS 'É a data de finalizacao da movimentação podendo o mesmo ter sido finalizado ou rejeitado';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_usuario_finalizacao" IS 'Usuário que finalizou a movimentação';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_dthr_envio" IS 'É a data de envio da movimentação';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_motivo_obs" IS 'É o motivo pela aquela movimentação está sendo registrada';

COMMENT ON COLUMN "pat_intangivel"."tb_movimentacao"."mo_motivo_rejeicao" IS 'É o motivo pela aquela movimentação foi rejeitada';

CREATE INDEX "in_pa_uo_id_orgao_origem" ON "pat_intangivel"."tb_movimentacao" ("uo_id_orgao_origem");

CREATE INDEX "in_pa_uo_id_orgao_destino" ON "pat_intangivel"."tb_movimentacao" ("uo_id_orgao_destino");

CREATE INDEX "in_pa_pa_id" ON "pat_intangivel"."tb_movimentacao" ("pa_id");

ALTER TABLE "pat_intangivel"."tb_movimentacao"  ADD CONSTRAINT "pk_tb_movimentacao" PRIMARY KEY ("mo_id");

-- Table pat_intangivel.tb_lancamentos_contabeis

  CREATE TABLE "pat_intangivel"."tb_lancamentos_contabeis"
  (
      "lc_id" Integer NOT NULL,
      "lc_data" timestamp(9),
      "lc_tipo_lancamento" Character varying (45) NOT NULL,
      "lc_motivo_lancamento" Character varying (45) NOT NULL,
      "lc_valor_liquido" Numeric(20,6),
      "pa_id" Integer,
      "mo_id" Integer,
      "uo_id_orgao" Integer,
      "cc_id" Integer,
      "lc_dthr_cadastro" timestamp(9),
      "lc_dthr_alteracao" timestamp(9),
      "lc_usuario_cadastro" Character varying (255),
      "lc_usuario_alteracao" character varying(255)
  )

      WITH (autovacuum_enabled=true);

  COMMENT ON TABLE "pat_intangivel"."tb_lancamentos_contabeis"  IS 'Cadastro dos lançamentos contábeis.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."pa_id" IS 'Chave estrangeira para o patrimonio.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."mo_id" IS 'Chave estrangeira para o movimentacao.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."uo_id_orgao" IS 'Chave estrangeira para o orgão a qual o lançamento contabíl pertence.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."lc_tipo_lancamento" IS 'Tipo do lançamento contabíl. Pode assumir os valores: CREDITO e DEBITO.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."lc_data" IS 'Data referente ao lançamento contábil.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."lc_motivo_lancamento" IS 'Motivo do lançamento contabíl. Pode assumir os valores: CANCELAMENTO_ATIVACAO, ATIVACAO, TRANSFERENCIA_DEFINITIVA e DOACAO_ORGAOS.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."lc_valor_liquido" IS 'Valor líquido do patrimônio no momento do lançamento contabíl.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."cc_id" IS 'Chave estrangeira para a conta contábil do lançamento contabíl.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."lc_dthr_cadastro" IS 'Data/Hora de criação do registro.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."lc_dthr_alteracao" IS 'Data/Hora da última alteração do registro.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."lc_usuario_cadastro" IS 'Usuário que criou o registro.';

  COMMENT ON COLUMN "pat_intangivel"."tb_lancamentos_contabeis"."lc_usuario_alteracao" IS 'Último usuário a alterar o registro.';

  CREATE INDEX "in_lc_uo_id_orgao" ON "pat_intangivel"."tb_lancamentos_contabeis"  ("uo_id_orgao");

  CREATE INDEX "in_lc_cc_id" ON "pat_intangivel"."tb_lancamentos_contabeis"  ("cc_id");

  CREATE INDEX "in_lc_pa_id" ON "pat_intangivel"."tb_lancamentos_contabeis"  ("pa_id");

  CREATE INDEX "in_lc_mo_id" ON "pat_intangivel"."tb_lancamentos_contabeis"  ("mo_id");

  ALTER TABLE "pat_intangivel"."tb_lancamentos_contabeis"  ADD CONSTRAINT "pk_tb_lancamentos_contabeis" PRIMARY KEY ("lc_id");

---


  COMMENT ON TABLE "pat_intangivel"."tb_historico_memorando"  IS 'Cadastro dos hitóricos dos memorandos.';

  COMMENT ON COLUMN "pat_intangivel"."tb_historico_memorando"."hm_numero_memorando" IS 'Número do memorando gerado.';

  COMMENT ON COLUMN "pat_intangivel"."tb_historico_memorando"."hm_uri_anexo" IS 'Caminho para o documento anexado.';

  COMMENT ON COLUMN "pat_intangivel"."tb_historico_memorando"."hm_dthr_cadastro" IS 'Data/Hora de criação do registro.';

  COMMENT ON COLUMN "pat_intangivel"."tb_historico_memorando"."hm_dthr_alteracao" IS 'Data/Hora da última alteração do registro.';

  COMMENT ON COLUMN "pat_intangivel"."tb_historico_memorando"."hm_usuario_cadastro" IS 'Usuário que criou o registro.';

  COMMENT ON COLUMN "pat_intangivel"."tb_historico_memorando"."hm_usuario_alteracao" IS 'Último usuário a alterar o registro.';


-- Create foreign keys (relationships) section -------------------------------------------------

  ALTER TABLE "pat_intangivel"."tb_movimentacao"  ADD CONSTRAINT "fk_patrimonio_movimentacao" FOREIGN KEY ("pa_id") REFERENCES "pat_intangivel"."tb_patrimonio" ("pa_id") ON DELETE NO ACTION ON UPDATE NO ACTION;

  ALTER TABLE "pat_intangivel"."tb_movimentacao"  ADD CONSTRAINT "fk_unidadeorg_origem_movimentacao" FOREIGN KEY ("uo_id_orgao_origem") REFERENCES "comum_siga"."tb_unidade_organizacional" ("uo_id") ON DELETE NO ACTION ON UPDATE NO ACTION;

  ALTER TABLE "pat_intangivel"."tb_movimentacao"  ADD CONSTRAINT "fk_unidadeorg_destino_movimentacao" FOREIGN KEY ("uo_id_orgao_destino") REFERENCES "comum_siga"."tb_unidade_organizacional" ("uo_id") ON DELETE NO ACTION ON UPDATE NO ACTION;

  ALTER TABLE "pat_intangivel"."tb_lancamentos_contabeis"  ADD CONSTRAINT "fk_pa_id_lancamentos_contabeis" FOREIGN KEY ("pa_id") REFERENCES "pat_intangivel"."tb_patrimonio" ("pa_id") ON DELETE NO ACTION ON UPDATE NO ACTION;

  ALTER TABLE "pat_intangivel"."tb_lancamentos_contabeis"  ADD CONSTRAINT "fk_mo_id_lancamentos_contabeis" FOREIGN KEY ("mo_id") REFERENCES "pat_intangivel"."tb_movimentacao" ("mo_id") ON DELETE NO ACTION ON UPDATE NO ACTION;

  ALTER TABLE "pat_intangivel"."tb_lancamentos_contabeis"  ADD CONSTRAINT "fk_unidadeorg_lancamentos_contabeis" FOREIGN KEY ("uo_id_orgao") REFERENCES "comum_siga"."tb_unidade_organizacional" ("uo_id") ON DELETE NO ACTION ON UPDATE NO ACTION;

  ALTER TABLE "pat_intangivel"."tb_lancamentos_contabeis"  ADD CONSTRAINT "fk_conta_lancamentos_contabeis" FOREIGN KEY ("cc_id") REFERENCES "comum_siga"."tb_conta_contabil" ("cc_id") ON DELETE NO ACTION ON UPDATE NO ACTION;

  ALTER TABLE "pat_intangivel"."tb_historico_memorando"  ADD CONSTRAINT "pk_tb_historico_memorando" PRIMARY KEY ("hm_id");
  -- Update Views section -------------------------------------------------

  DROP VIEW IF EXISTS pat_intangivel.vi_relatorio_analitico;
  DROP VIEW IF EXISTS pat_intangivel.vi_relatorio_sintetico;

  CREATE OR replace VIEW pat_intangivel.vi_relatorio_sintetico AS
  SELECT pa.pa_id              AS rs_patrimonio,
         pa.uo_id_orgao        AS rs_orgao,
         pa.pa_valor_aquisicao AS rs_valor_aquisicao,
         pa.pa_dthr_ativacao   AS rs_pa_ativacao,
         cc.cc_id              AS rs_conta_contabil_id,
         am.am_valor_subtraido AS rs_valor_subtra_amort,
         am.am_valor_posterior AS rs_valor_liquido,
         am.am_dthr_final   AS rs_data_amort,
         am.uo_id_orgao   AS rs_uo_id_orgao_amort,
         row_number() OVER () AS rs_id
  FROM pat_intangivel.tb_patrimonio pa
           LEFT JOIN comum_siga.tb_conta_contabil cc ON pa.cc_id = cc.cc_id
           LEFT JOIN pat_intangivel.tb_amortizacao am ON pa.pa_id = am.pa_id
  WHERE pa.pa_situacao = 'ATIVO';
