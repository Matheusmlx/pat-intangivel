--UPDATE QUERY IN TABLE "PAT_INTANGIVEL"."TB_DOCUMENTO"
alter table "pat_intangivel"."tb_documento" add column  "mo_id" Integer;

comment on column "pat_intangivel"."tb_documento"."mo_id" is 'Chave estrangeira para a tabela de Movimentação';

CREATE INDEX "in_mo_id" ON "pat_intangivel"."tb_documento" ("mo_id");

--Create foreign keys relationships--------------------------
ALTER TABLE "pat_intangivel"."tb_documento" ADD CONSTRAINT "fk_movimentacao_documento" FOREIGN KEY ("mo_id") REFERENCES "pat_intangivel"."tb_movimentacao" ("mo_id") ON DELETE NO ACTION ON UPDATE NO ACTION;
