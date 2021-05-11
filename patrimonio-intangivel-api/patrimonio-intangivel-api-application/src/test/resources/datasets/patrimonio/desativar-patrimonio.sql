insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia)
values (10000, 'teste ativacao', 'sigla', 'ATIVO', 'FORMAL', '0001');

insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao)
values (2, '124110200', 'BENS INTANGIVEIS>SOFTWARE', 'ATIVO');

INSERT INTO pat_intangivel.tb_config_amortizacao (ca_id, ca_metodo, ca_vida_util_meses, ca_situacao, ca_taxa,
                                                  ca_percentual_residual, ca_tipo, cc_id)
VALUES (40, 'QUOTAS_CONSTANTES', 47, 'ATIVO', 2, 0, 'AMORTIZAVEL', 2);

INSERT INTO pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_numero, pa_descricao, pa_situacao, pa_estado,
                                          pa_valor_liquido, pa_valor_aquisicao, pa_reconhecimento, pa_dthr_aquisicao,
                                          pa_dthr_nl, pa_numero_nl,
                                          pa_dthr_inicio_vida_util, pa_dthr_vencimento, pa_meses_vida_util,
                                          pa_dthr_fim_vida_util, uo_id_orgao, uo_id_setor,
                                          cc_id, pa_dthr_cadastro, pa_dthr_alteracao, pa_usuario_cadastro,
                                          pa_usuario_alteracao, pa_dthr_ativacao, pa_vida_indefinida,
                                          pa_dthr_final_ativacao)
VALUES (68, 'SOFTWARES', 'Ativar Este', '0000000001', 'Teste de ativação', 'ATIVO', 'PRONTO_PARA_USO', 150000.000000,
        150000.000000, 'AQUISICAO_SEPARADA', '2020-02-24 03:00:00.000000', '2020-02-24 03:00:00.000000', '1111NL111111',
        '2020-02-27 11:59:23.144594', '2024-02-26 03:00:00.000000', 47, '2024-02-26 03:00:00.000000', 10000, 10000, 2,
        '2020-02-24 14:08:15.977983', '2020-02-27 11:59:24.465973', 'admin', 'admin', CURRENT_DATE, false,
        CURRENT_DATE);

INSERT INTO pat_intangivel.tb_lancamentos_contabeis (lc_id, lc_data, lc_tipo_lancamento, lc_motivo_lancamento,
                                                     lc_valor_liquido, pa_id, mo_id, uo_id_orgao, cc_id,
                                                     lc_dthr_cadastro, lc_dthr_alteracao, lc_usuario_cadastro,
                                                     lc_usuario_alteracao)
VALUES (2, '2020-12-04 14:17:48.227620', 'CREDITO', 'ATIVACAO', 150000.000000, 68, null, 10000, 2,
        '2020-12-04 14:17:48.288000', '2020-12-04 14:17:48.288000', 'admin', null);

INSERT INTO pat_intangivel.tb_dados_amortizacao (da_id, ca_id, pa_id, da_dthr_cadastro, da_dthr_alteracao)
VALUES (15, 40, 68, '2020-02-24 14:08:15.977983', '2020-02-27 11:59:24.465973');

UPDATE pat_intangivel.tb_patrimonio
SET da_id = 15
WHERE pa_id = 68;
