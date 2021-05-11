insert into pat_intangivel.tb_nota_lancto_contabil(NLC_ID,NLC_NUMERO) values (2,'2121NL212112');

insert into pat_intangivel.tb_patrimonio (PA_ID, PA_TIPO,NLC_ID, PA_NOME, PA_ESTADO, PA_SITUACAO)
values (2, 'SOFTWARES',2, 'Office', 'EM_DESENVOLVIMENTO', 'EM_ELABORACAO');

insert into pat_intangivel.tb_patrimonio (PA_ID, PA_TIPO, PA_NOME, PA_ESTADO, PA_SITUACAO)
values (3, 'SOFTWARES', 'Alura', 'EM_DESENVOLVIMENTO', 'EM_ELABORACAO');

insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao)
values (2, '124110200', 'BENS INTANGIVEIS>SOFTWARE EM DESENVOLVIMENTO', 'ATIVO');
