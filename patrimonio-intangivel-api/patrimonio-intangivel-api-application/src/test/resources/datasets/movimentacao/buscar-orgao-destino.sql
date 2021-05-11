INSERT INTO comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_tipo_adm,
                                                  uo_cod_hierarquia, uo_almoxarifado, uo_id_superior)
VALUES (129, 'Agência Estadual de Metrologia', 'AEM-MS', 'ATIVO', 'FORMAL', 'AUTARQUIA', '0002.0005.0007', false, 101);


insert into pat_intangivel.tb_patrimonio (PA_ID, PA_TIPO, PA_NOME, PA_ESTADO, PA_SITUACAO)
values (2, 'SOFTWARES', 'Office', 'EM_DESENVOLVIMENTO', 'ATIVO');


insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO,MO_TIPO,PA_ID, UO_ID_ORGAO_ORIGEM)
values (1,'0001','DOACAO_ENTRE_ORGAOS', 2, 129);

