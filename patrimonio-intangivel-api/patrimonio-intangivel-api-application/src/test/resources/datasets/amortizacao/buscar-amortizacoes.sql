insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_situacao) values (10, 'SOFTWARES', 'Office', 'EM_ELABORACAO');

insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao) values (1, '124110100', 'BENS INTANGIVEIS>SOFTWARE', 'ATIVO' );

INSERT INTO pat_intangivel.tb_config_amortizacao (ca_id, ca_metodo, ca_vida_util_meses, ca_situacao, ca_taxa, ca_percentual_residual, ca_tipo, cc_id, ca_dthr_cadastro, ca_dthr_alteracao, ca_usuario_cadastro, ca_usuario_alteracao) VALUES (1, 'QUOTAS_CONSTANTES', 7, 'ATIVO', 14.290000, 0.000000, 'AMORTIZAVEL', 1, '2020-07-09 10:39:10.242000', null, 'admin', null);

INSERT INTO pat_intangivel.tb_amortizacao (am_id, am_dthr_inicial, am_dthr_final, am_valor_anterior, am_valor_posterior, am_valor_subtraido, am_taxa_aplicada, pa_id, ca_id, am_dthr_cadastro, am_dthr_alteracao, am_usuario_cadastro, am_usuario_alteracao) VALUES (1, '2020-01-07 00:00:00.000000', '2020-01-31 23:59:59.000000', 1000.000000, 881.000000, 119.000000, 11.900000, 10, 1, '2020-07-09 10:39:21.221000', null, 'admin', null);
INSERT INTO pat_intangivel.tb_amortizacao (am_id, am_dthr_inicial, am_dthr_final, am_valor_anterior, am_valor_posterior, am_valor_subtraido, am_taxa_aplicada, pa_id, ca_id, am_dthr_cadastro, am_dthr_alteracao, am_usuario_cadastro, am_usuario_alteracao) VALUES (2, '2020-02-01 00:00:00.000000', '2020-02-29 23:59:59.000000', 881.000000, 738.100000, 142.900000, 14.290000, 10, 1, '2020-07-09 10:39:21.350000', null, 'admin', null);
INSERT INTO pat_intangivel.tb_amortizacao (am_id, am_dthr_inicial, am_dthr_final, am_valor_anterior, am_valor_posterior, am_valor_subtraido, am_taxa_aplicada, pa_id, ca_id, am_dthr_cadastro, am_dthr_alteracao, am_usuario_cadastro, am_usuario_alteracao) VALUES (3, '2020-03-01 00:00:00.000000', '2020-03-31 23:59:59.000000', 738.100000, 595.200000, 142.900000, 14.290000, 10, 1, '2020-07-09 10:39:21.486000', null, 'admin', null);
INSERT INTO pat_intangivel.tb_amortizacao (am_id, am_dthr_inicial, am_dthr_final, am_valor_anterior, am_valor_posterior, am_valor_subtraido, am_taxa_aplicada, pa_id, ca_id, am_dthr_cadastro, am_dthr_alteracao, am_usuario_cadastro, am_usuario_alteracao) VALUES (4, '2020-04-01 00:00:00.000000', '2020-04-30 23:59:59.000000', 595.200000, 452.300000, 142.900000, 14.290000, 10, 1, '2020-07-09 10:39:21.603000', null, 'admin', null);
INSERT INTO pat_intangivel.tb_amortizacao (am_id, am_dthr_inicial, am_dthr_final, am_valor_anterior, am_valor_posterior, am_valor_subtraido, am_taxa_aplicada, pa_id, ca_id, am_dthr_cadastro, am_dthr_alteracao, am_usuario_cadastro, am_usuario_alteracao) VALUES (5, '2020-05-01 00:00:00.000000', '2020-05-31 23:59:59.000000', 452.300000, 309.400000, 142.900000, 14.290000, 10, 1, '2020-07-09 10:39:21.716000', null, 'admin', null);
INSERT INTO pat_intangivel.tb_amortizacao (am_id, am_dthr_inicial, am_dthr_final, am_valor_anterior, am_valor_posterior, am_valor_subtraido, am_taxa_aplicada, pa_id, ca_id, am_dthr_cadastro, am_dthr_alteracao, am_usuario_cadastro, am_usuario_alteracao) VALUES (6, '2020-06-01 00:00:00.000000', '2020-06-30 23:59:59.000000', 309.400000, 166.500000, 142.900000, 14.290000, 10, 1, '2020-07-09 10:39:21.837000', null, 'admin', null);
