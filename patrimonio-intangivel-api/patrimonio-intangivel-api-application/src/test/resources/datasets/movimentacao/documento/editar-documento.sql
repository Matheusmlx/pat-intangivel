insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
(nextval('comum.seq_usuario'), 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into pat_intangivel.tb_nota_lancto_contabil(nlc_id,nlc_numero) values (2,'2121NL212112');

insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_descricao,nlc_id, pa_situacao, pa_estado, pa_valor_aquisicao, pa_reconhecimento)
values (1, 'SOFTWARES', 'Intellij', 'Descricao de software',2, 'EM_ELABORACAO', 'PRONTO_PARA_USO', 1000.00, 'AQUISICAO_SEPARADA');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_DTHR_ALTERACAO,MO_SITUACAO,MO_MOTIVO_OBS)
values (1,'0001','DOACAO_ENTRE_ORGAOS',1,'2020-05-15 13:16:54.216000','AGUARDANDO_RECEBIMENTO','Patrimonio não está mais em uso no orgão atual');

insert into comum_siga.tb_tipo_documento (td_id,td_desc,td_permite_anexo,td_dthr_cadastro,td_dthr_alteracao,td_usuario_cadastro,td_usuario_alteracao,td_identificacao_documento) values (1, 'Contrato',true,null,null,null,null,'CONTRATO');
insert into pat_intangivel.tb_documento (do_id,do_numero,do_dt,do_valor,do_uri_anexo,pa_id,td_id,do_dthr_cadastro,do_dthr_alteracao,mo_id) values (1,'1','2020-06-02',1200550000,'repo1:patrimoniointangivel/teste.pdf',1,1,'2020-06-25 11:36:56.350000',null,1);
