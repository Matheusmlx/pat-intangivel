insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values (1, 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into pat_intangivel.tb_patrimonio (PA_ID, PA_TIPO, PA_NOME, PA_ESTADO, PA_SITUACAO)
values (1, 'SOFTWARES', 'Office', 'EM_DESENVOLVIMENTO', 'EM_ELABORACAO');

insert into pat_intangivel.tb_patrimonio (PA_ID, PA_TIPO, PA_NOME, PA_ESTADO, PA_SITUACAO)
values (2, 'SOFTWARES', 'Office', 'EM_DESENVOLVIMENTO', 'EM_ELABORACAO');

insert into comum_siga.tb_unidade_organizacional (UO_ID, UO_NOME, UO_SIGLA, UO_SITUACAO, UO_TIPO, UO_COD_HIERARQUIA)
values (4554, 'teste', 'AEMS', 'ATIVO', 'FORMAL', '0001');

insert into comum_siga.tb_unidade_organizacional (UO_ID, UO_NOME, UO_SIGLA, UO_SITUACAO, UO_TIPO, UO_COD_HIERARQUIA)
values (5804, 'outro teste', 'FUNDESPORT', 'ATIVO', 'FORMAL', '0001');

insert into comum_siga.tb_unidade_organizacional (UO_ID, UO_NOME, UO_SIGLA, UO_SITUACAO, UO_TIPO, UO_COD_HIERARQUIA)
values (5522, 'outro teste', 'DETRAN', 'ATIVO', 'FORMAL', '0001');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_DTHR_ALTERACAO,MO_SITUACAO,MO_MOTIVO_OBS,MO_USUARIO_CADASTRO,UO_ID_ORGAO_ORIGEM,UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (1,'0001','DOACAO_ENTRE_ORGAOS',1,'2020-05-15 13:16:54.216000','AGUARDANDO_RECEBIMENTO','Patrimonio não está mais em uso no orgão atual','Goku',5804,4554, 'adminstrador do sistema');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,MO_SITUACAO,PA_ID,MO_DTHR_ALTERACAO,UO_ID_ORGAO_ORIGEM, UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (2,'0002','DOACAO_ENTRE_ORGAOS','FINALIZADO',1,'2020-06-15 13:16:54.216000',5804,5522, 'adminstrador do sistema');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,MO_SITUACAO,PA_ID,MO_DTHR_ALTERACAO,UO_ID_ORGAO_ORIGEM, UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (3,'0003','DOACAO_ENTRE_ORGAOS','EM_ELABORACAO',1,'2020-07-15 13:16:54.216000',5804,4554, 'adminstrador do sistema');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_DTHR_ALTERACAO,UO_ID_ORGAO_ORIGEM, UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (4,'0004','DOACAO_ENTRE_ORGAOS',1,'2020-09-15 13:16:54.216000',5804,5522, 'adminstrador do sistema');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_DTHR_ALTERACAO,UO_ID_ORGAO_ORIGEM, UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (5,'0005','DOACAO_PARA_TERCEIROS',1,'2020-10-15 13:16:54.216000',5804,4554, 'adminstrador do sistema');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,MO_SITUACAO,PA_ID,MO_DTHR_ALTERACAO,UO_ID_ORGAO_ORIGEM, UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (6,'0006','DOACAO_ENTRE_ORGAOS','AGUARDANDO_RECEBIMENTO',1,'2020-10-20 13:16:54.216000',5804,4554, 'adminstrador do sistema');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_DTHR_ALTERACAO,UO_ID_ORGAO_ORIGEM, UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (7,'0007','DOACAO_PARA_TERCEIROS',1,'2020-09-21 13:16:54.216000',5804,4554, 'adminstrador do sistema');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_DTHR_ALTERACAO,UO_ID_ORGAO_ORIGEM, UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (8,'0008','DOACAO_ENTRE_ORGAOS',1,'2020-09-22 13:16:54.216000',5804,5522, 'adminstrador do sistema');

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_DTHR_ALTERACAO,UO_ID_ORGAO_ORIGEM, UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (9,'0009','DOACAO_PARA_TERCEIROS',1,'2020-09-23 13:16:54.216000',5804,4554, 'adminstrador do sistema');
