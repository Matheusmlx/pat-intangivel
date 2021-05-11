insert into comum_siga.tb_tipo_documento (td_id,td_desc,td_permite_anexo,td_identificacao_documento) values (1,'Contrato',true,'CONTRATO');

insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_situacao) values (1, 'SOFTWARES', 'Office', 'EM_ELABORACAO');

insert into comum_siga.tb_unidade_organizacional (UO_ID, UO_NOME, UO_SIGLA, UO_SITUACAO, UO_TIPO, UO_COD_HIERARQUIA)
values (5522, 'outro teste', 'DETRAN', 'ATIVO', 'FORMAL', '0001');

insert into comum_siga.tb_unidade_organizacional (UO_ID, UO_NOME, UO_SIGLA, UO_SITUACAO, UO_TIPO, UO_COD_HIERARQUIA)
values (4554, 'teste', 'AEMS', 'ATIVO', 'FORMAL', '0001');

insert into comum_siga.tb_unidade_organizacional (UO_ID, UO_NOME, UO_SIGLA, UO_SITUACAO, UO_TIPO, UO_COD_HIERARQUIA)
values (5804, 'outro teste', 'FUNDESPORT', 'ATIVO', 'FORMAL', '0001');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_DTHR_ALTERACAO,MO_SITUACAO,MO_MOTIVO_OBS,MO_USUARIO_CADASTRO,UO_ID_ORGAO_ORIGEM,UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (1,'0001','DOACAO_ENTRE_ORGAOS',1,'2020-05-15 13:16:54.216000','AGUARDANDO_RECEBIMENTO','Patrimonio não está mais em uso no orgão atual','Goku',5804,4554, 'adminstrador do sistema');

insert into pat_intangivel.tb_documento (do_id,do_numero,do_dt,do_valor,do_uri_anexo,pa_id,td_id,do_dthr_cadastro,do_dthr_alteracao,mo_id) values (1,12,'2020-06-02',1200550000,'repo1:patrimoniointangivel/nh.jpg',1,1,'2020-06-25 11:36:56.350000',null,1);

insert into pat_intangivel.tb_documento (do_id,do_numero,do_dt,do_valor,do_uri_anexo,pa_id,td_id,do_dthr_cadastro,do_dthr_alteracao,mo_id) values (2,3214,'2020-06-02',1200550000,'repo1:patrimoniointangivel/nh.jpg',1,1,'2020-06-25 11:36:56.350000',null,1);

insert into pat_intangivel.tb_documento (do_id,do_numero,do_dt,do_valor,do_uri_anexo,pa_id,td_id,do_dthr_cadastro,do_dthr_alteracao,mo_id) values (3,3214,'2020-06-02',1200550000,'repo1:patrimoniointangivel/nh.jpg',1,1,'2020-06-25 11:36:56.350000',null,null);
