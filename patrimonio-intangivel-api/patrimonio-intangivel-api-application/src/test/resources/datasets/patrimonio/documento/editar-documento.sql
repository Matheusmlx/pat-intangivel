insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values
    (nextval('comum.seq_usuario'), 'usuario', 'usuario1@azi.com.br', '57708514738', 'Fornecedor Google', FALSE, '33266655', 'ATIVO', 1, 30, 'EXTERNO');


insert into comum_siga.tb_unidade_organizacional (uo_id, uo_nome, uo_sigla, uo_situacao, uo_tipo, uo_cod_hierarquia)
values (10000, 'teste ativacao', 'sigla', 'ATIVO', 'FORMAL', '0001');

insert into comum_siga.tb_conta_contabil (cc_id, cc_codigo, cc_descricao, cc_situacao)
values (2, '124110100', 'BENS INTANGIVEIS>SOFTWARE', 'ATIVO');

insert into pat_intangivel.tb_config_conta_contabil (ccc_id, ccc_metodo, ccc_tipo, cc_id) values (3, 'QUOTAS_CONSTANTES', 'AMORTIZAVEL', 2);

insert into pat_intangivel.tb_patrimonio (pa_id, pa_tipo, pa_nome, pa_descricao, pa_situacao, pa_estado,
                                          pa_valor_aquisicao, pa_reconhecimento, pa_dthr_aquisicao,
                                          pa_dthr_nl, pa_numero_nl, pa_dthr_vencimento, uo_id_orgao, uo_id_setor, pa_vida_indefinida, cc_id)
values (1, 'SOFTWARES', 'Intellij', 'Descricao de software', 'EM_ELABORACAO', 'PRONTO_PARA_USO', 1000.00,
        'AQUISICAO_SEPARADA', '2012-06-18T10:34:09', '2012-06-18T10:34:09', '1111NL111111', '2022-06-18T10:34:09', 10000, 10000, false, 2);

insert into pat_intangivel.tb_movimentacao (MO_ID, MO_CODIGO, MO_TIPO, PA_ID, MO_USUARIO_FINALIZACAO)
values (1, '0001', 'DOACAO_ENTRE_ORGAOS', 1, 'adminstrador do sistema');

insert into comum_siga.tb_tipo_documento (td_id,td_desc,td_permite_anexo,td_identificacao_documento) values (1,'Contrato',true,'CONTRATO');
insert into pat_intangivel.tb_documento (do_id,do_numero,do_dt,do_valor,do_uri_anexo,pa_id,td_id,do_dthr_cadastro,do_dthr_alteracao,mo_id) values (1,'454454','2020-06-02',1200550000,'repo1:patrimoniointangivel/teste.pdf',1,1,'2020-06-25 11:36:56.350000',null,1);

