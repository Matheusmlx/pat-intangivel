-- create sequences section ------------------------------------------------
create sequence "pat_intangivel"."seq_nota_lancto_contabil"
    increment by 1
    start with 1
    no maxvalue
    no minvalue
    cache 1;

CREATE TABLE "pat_intangivel"."tb_nota_lancto_contabil"
(
    "nlc_id" integer not null,
    "nlc_numero" character varying(45),
    "nlc_dthr_lancamento" timestamp(9),
    "nlc_dthr_cadastro" timestamp (9),
    "nlc_dthr_alteracao" timestamp (9),
    "nlc_usuario_cadastro" character varying(255),
    "nlc_usuario_alteracao" character varying(255)
)
  with (autovacuum_enabled=true);

comment on table "pat_intangivel"."tb_nota_lancto_contabil" is 'Armazena as informações de um registro de nota de lançamento contábil';
comment on column "pat_intangivel"."tb_nota_lancto_contabil"."nlc_id" is 'chave primária da tabela nota de lançamento';
comment on column "pat_intangivel"."tb_nota_lancto_contabil"."nlc_numero" is 'armazena o número da nota de lançamento contábil';
comment on column "pat_intangivel"."tb_nota_lancto_contabil"."nlc_dthr_lancamento" is 'data/hora em que foi realizado o lançamento contábil';
comment on column "pat_intangivel"."tb_nota_lancto_contabil"."nlc_dthr_cadastro" is 'campo de auditoria que guarda a data de cadastro ';
comment on column "pat_intangivel"."tb_nota_lancto_contabil"."nlc_dthr_alteracao" is 'campo de auditoria que guarda a data de edição';
comment on column "pat_intangivel"."tb_nota_lancto_contabil"."nlc_usuario_cadastro" is 'campo de auditoria que guarda o usuário que realizou o cadastro';
comment on column "pat_intangivel"."tb_nota_lancto_contabil"."nlc_usuario_alteracao" is 'campo de auditoria que guarda o usuário que realizou a edição';

alter table "pat_intangivel"."tb_nota_lancto_contabil" add constraint "pk_tb_nota_lancto_contabil" primary key ("nlc_id");

--UPDATE QUERY IN TABLE "PAT_INTANGIVEL"."TB_PATRIMONIO"
alter table "pat_intangivel"."tb_patrimonio" add column "nlc_id" Integer;

comment on column "pat_intangivel"."tb_patrimonio"."nlc_id" is 'Chave estrangeira para a tabela Nota de Lançamento Contábil';

create index "in_in_nlc_id" on "pat_intangivel"."tb_patrimonio" ("nlc_id");

--UPDATE QUERY IN TABLE "PAT_INTANGIVEL"."TB_MOVIMENTACAO"
alter table "pat_intangivel"."tb_movimentacao" add column "nlc_id" Integer;

comment on column "pat_intangivel"."tb_movimentacao"."nlc_id" is 'Chave estrangeira para a tabela Nota de Lançamento Contábil';

create index "in_mo_nlc_id" on "pat_intangivel"."tb_movimentacao" ("nlc_id");
