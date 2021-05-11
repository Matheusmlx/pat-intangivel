CREATE OR replace VIEW pat_intangivel.vi_relatorio_analitico AS
SELECT pa.uo_id_orgao AS ra_orgao,
       pa.cc_id AS ra_conta_contabil,
       pa.pa_id AS ra_patrimonio,
       pa.pa_numero AS ra_numeroPatrimonio,
       pa.pa_nome AS ra_nomePatrimonio,
       pa.pa_tipo AS ra_tipoPatrimonio,
       pa.pa_amortizavel AS ra_amortizavel,
       pa.pa_valor_aquisicao AS ra_valorAquisicao,
       am.am_valor_subtraido AS ra_valor_subtra_amort,
       (pa.pa_valor_aquisicao - am.am_valor_posterior) AS ra_amortizacaoAcumulada,
       am.am_valor_posterior AS ra_valor_liquido,
       pa.pa_dthr_ativacao AS ra_pa_ativacao,
       am.am_dthr_final   AS ra_data_amort,
       row_number() OVER () AS ra_id
FROM pat_intangivel.tb_patrimonio pa
         LEFT JOIN pat_intangivel.tb_amortizacao am ON pa.pa_id = am.pa_id
WHERE pa.pa_situacao = 'ATIVO';
