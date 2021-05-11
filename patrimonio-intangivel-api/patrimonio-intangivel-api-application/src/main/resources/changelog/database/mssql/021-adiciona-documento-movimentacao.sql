--UPDATE QUERY IN TABLE "PAT_INTANGIVEL"."TB_DOCUMENTO"

ALTER TABLE pat_intangivel.TB_DOCUMENTO ADD COLUMN  MO_ID INT;

EXEC sp_addextendedproperty 'MS_Description', 'Chave estrangeira para a tabela de Movimentação', 'TABLE', 'TB_DOCUMENTO', 'COLUMN', 'MO_ID'
go

CREATE INDEX IN_MO_ID ON pat_intangivel.TB_DOCUMENTO (MO_ID)
go

--ADD RELAÇÃO DE CHAVE ESTRANGEIRA----

ALTER TABLE pat_intangivel.TB_DOCUMENTO ADD CONSTRAINT FK_MOVIMENTACAO_DOCUMENTO FOREIGN  KEY (MO_ID) REFERENCES pat_intangivel.TB_MOVIMENTACAO (MO_ID) ON UPDATE NO ACTION ON DELETE NO ACTION
go