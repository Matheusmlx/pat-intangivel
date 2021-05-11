CREATE OR replace VIEW pat_intangivel.vi_relatorio_sintetico AS
SELECT pa.pa_id              AS rs_patrimonio,
       pa.uo_id_orgao        AS rs_orgao,
       pa.pa_valor_aquisicao AS rs_valor_aquisicao,
       pa.pa_dthr_ativacao   AS rs_pa_ativacao,
       cc.cc_id              AS rs_conta_contabil_id,
       am.am_valor_subtraido AS rs_valor_subtra_amort,
       am.am_valor_posterior AS rs_valor_liquido,
       am.am_dthr_final   AS rs_data_amort,
       row_number() OVER () AS rs_id
FROM pat_intangivel.tb_patrimonio pa
         LEFT JOIN comum_siga.tb_conta_contabil cc
                   ON pa.cc_id = cc.cc_id
         LEFT JOIN pat_intangivel.tb_amortizacao am ON pa.pa_id = am.pa_id
WHERE pa.pa_situacao = 'ATIVO';
