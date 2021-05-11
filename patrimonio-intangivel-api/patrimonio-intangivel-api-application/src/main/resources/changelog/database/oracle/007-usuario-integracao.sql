INSERT INTO comum.tb_usuario (us_id, us_email, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, id_id, pa_id, us_dthr_cadastro, us_usuario_cadastro)
VALUES (comum.seq_usuario.nextval, 'integracaopat', '35380785646', 'Usuário da integração', 0, '556733032700', 1, 30, current_timestamp, 'Sistema');

INSERT INTO hal.tb_usuario_hal (us_id, us_senha)
SELECT us_id, 'UTr6YlcHzuQ='
FROM comum.tb_usuario
WHERE us_id = (SELECT us_id FROM comum.tb_usuario WHERE us_email = 'integracaopat');
