insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values (1, 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia)
values (100, 'AGEPEN', 'sigla100', 'ATIVO', 'FORMAL', '0001');

insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia)
values (200, 'SAD', 'sigla200', 'ATIVO', 'FORMAL', '0002');

insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_dthr_vencimento, pa_situacao, uo_id_orgao) values (10, 'SOFTWARES', 'Office 10', '2030-01-24 14:00:00.000001', 'ATIVO', 100);
insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_dthr_vencimento, pa_situacao, uo_id_orgao) values (11, 'SOFTWARES', 'Office 11', '2030-02-24 14:00:00.000001','EM_ELABORACAO', 100);
insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_dthr_vencimento, pa_situacao, uo_id_orgao) values (12, 'SOFTWARES', 'Office 12', '2030-03-24 14:00:00.000001','ATIVO', 200);
insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_dthr_vencimento, pa_situacao, uo_id_orgao) values (13, 'SOFTWARES', 'Office 13', '2030-04-24 14:00:00.000001','ATIVO', 200);
insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_dthr_vencimento, pa_situacao, uo_id_orgao) values (14, 'SOFTWARES', 'Office 14', '2030-05-24 14:00:00.000001','EM_ELABORACAO', 200);
insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_dthr_vencimento, pa_situacao, uo_id_orgao) values (15, 'SOFTWARES', 'Office 15', '2030-06-24 14:00:00.000001','EM_ELABORACAO', null);
