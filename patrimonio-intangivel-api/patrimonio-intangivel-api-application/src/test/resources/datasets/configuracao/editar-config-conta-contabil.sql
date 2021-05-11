insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao) values (1, '124110100', 'BENS INTANGIVEIS>SOFTWARE', 'ATIVO' );

INSERT INTO pat_intangivel.tb_config_amortizacao (ca_id, ca_metodo, ca_vida_util_meses, ca_situacao, ca_taxa, ca_percentual_residual, ca_tipo, cc_id, ca_dthr_cadastro, ca_dthr_alteracao, ca_usuario_cadastro, ca_usuario_alteracao) VALUES (1, 'QUOTAS_CONSTANTES', 7, 'ATIVO', 14.290000, 0.000000, 'AMORTIZAVEL', 1, '2020-07-09 10:39:10.242000', null, 'admin', null);

INSERT INTO pat_intangivel.tb_config_conta_contabil (ccc_id, ccc_metodo, ccc_tipo, cc_id, ccc_dthr_cadastro, ccc_dthr_alteracao, ccc_usuario_cadastro, ccc_usuario_alteracao) VALUES (1, 'QUOTAS_CONSTANTES', 'AMORTIZAVEL', 1, '2020-07-09 10:21:38.158000', null, 'admin', null);
