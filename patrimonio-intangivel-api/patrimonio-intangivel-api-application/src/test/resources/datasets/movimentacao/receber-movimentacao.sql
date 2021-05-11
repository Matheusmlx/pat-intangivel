insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia)
values (5, 'teste ativacao', 'sigla', 'ATIVO', 'FORMAL', '0001');

insert into pat_intangivel.tb_patrimonio (PA_ID, PA_TIPO, PA_NOME, PA_ESTADO, PA_SITUACAO,PA_VALOR_ENTRADA)
values (1, 'SOFTWARES', 'Office', 'EM_DESENVOLVIMENTO', 'EM_ELABORACAO',null);
insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID,MO_SITUACAO,UO_ID_ORGAO_DESTINO, MO_USUARIO_FINALIZACAO)
values (1,'0001','DOACAO_ENTRE_ORGAOS',1,'AGUARDANDO_RECEBIMENTO',5, 'adminstrador do sistema');
