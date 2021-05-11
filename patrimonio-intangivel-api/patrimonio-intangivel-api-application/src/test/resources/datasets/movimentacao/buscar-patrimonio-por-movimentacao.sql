insert into comum_siga.tb_unidade_organizacional (UO_ID, UO_NOME, UO_SIGLA, UO_SITUACAO, UO_TIPO, UO_COD_HIERARQUIA)
values (10000, 'teste setor', 'sigla', 'ATIVO', 'FORMAL', '0001');

insert into pat_intangivel.tb_patrimonio (PA_ID, PA_TIPO, PA_NOME, PA_ESTADO, PA_SITUACAO, PA_NUMERO, PA_DESCRICAO, UO_ID_SETOR)
values (1, 'SOFTWARES', 'Patrimonio Teste', 'EM_DESENVOLVIMENTO', 'EM_ELABORACAO', '000001', 'Agência de Regulação dos Serviços Públicos Delegados de Campo Grande', 10000);

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO, MO_TIPO, PA_ID, MO_DTHR_ALTERACAO, MO_SITUACAO, MO_MOTIVO_OBS, MO_USUARIO_CADASTRO, MO_USUARIO_FINALIZACAO)
values (1, '0001', 'DOACAO_ENTRE_ORGAOS', 1, '2020-05-15 13:16:54.216000', 'AGUARDANDO_RECEBIMENTO', 'Patrimonio não está mais em uso no orgão atual', 'Admin', 'adminstrador do sistema');
