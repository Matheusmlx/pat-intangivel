CREATE VIEW [PAT_INTANGIVEL].[VI_RELATORIO_SINTETICO]
    AS SELECT
        pa.pa_id              AS rs_patrimonio,
        pa.uo_id_orgao        AS rs_orgao,
        pa.pa_valor_aquisicao AS rs_valor_aquisicao,
        pa.pa_dthr_ativacao   AS rs_pa_ativacao,
        cc.cc_id              AS rs_conta_contabil_id,
        am.am_valor_subtraido AS rs_valor_subtra_amort,
        am.am_valor_posterior AS rs_valor_liquido,
        am.am_dthr_final   AS rs_data_amort,
        row_number() OVER (ORDER BY (SELECT NULL)) AS rs_id
FROM PAT_INTANGIVEL.TB_PATRIMONIO pa
         LEFT JOIN COMUM_SIGA.TB_CONTA_CONTABIL cc
                   ON pa.cc_id = cc.cc_id
         LEFT JOIN PAT_INTANGIVEL.TB_AMORTIZACAO am ON pa.pa_id = am.pa_id
WHERE pa.pa_situacao = 'ATIVO';
GO

