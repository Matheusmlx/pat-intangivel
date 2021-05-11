insert into pat_intangivel.tb_patrimonio (PA_ID, PA_TIPO, PA_NOME, PA_ESTADO, PA_SITUACAO, PA_AMORTIZAVEL, PA_DTHR_ATIVACAO, PA_DTHR_FIM_VIDA_UTIL)
values (1, 'SOFTWARES', 'Office', 'EM_DESENVOLVIMENTO', 'EM_ELABORACAO', 'TRUE', '2020-07-09 10:39:10.242000', '2020-11-30 23:59:59.000000');


insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao)
values (1, '124110100', 'BENS INTANGIVEIS>SOFTWARE', 'ATIVO');

INSERT INTO pat_intangivel.tb_config_amortizacao (ca_id, ca_metodo, ca_vida_util_meses, ca_situacao, ca_taxa,
                                                  ca_percentual_residual, ca_tipo, cc_id, ca_dthr_cadastro,
                                                  ca_dthr_alteracao, ca_usuario_cadastro, ca_usuario_alteracao)
VALUES (1, 'QUOTAS_CONSTANTES', 7, 'ATIVO', 14.290000, 0.000000, 'AMORTIZAVEL', 1, '2020-07-09 10:39:10.242000', null,
        'admin', null);


INSERT INTO pat_intangivel.tb_amortizacao (am_id, am_dthr_inicial, am_dthr_final, am_valor_anterior, am_valor_posterior,
                                           am_valor_subtraido, am_taxa_aplicada, pa_id, ca_id, am_dthr_cadastro,
                                           am_dthr_alteracao, am_usuario_cadastro, am_usuario_alteracao)
VALUES (1, '2020-01-07 00:00:00.000000', '2020-11-30 23:59:59.000000', 1000.000000, 881.000000, 119.000000, 11.900000,
        1, 1, '2020-07-09 10:39:21.221000', null, 'admin', null);

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO, MO_TIPO, PA_ID, MO_USUARIO_FINALIZACAO)
values (1, '0001', 'DOACAO_ENTRE_ORGAOS', 1, 'adminstrador do sistema');
