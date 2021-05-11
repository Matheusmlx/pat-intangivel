-- FORNECEDORES
insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values (1, 'Google Brasil Internet Ltda', '06990590000123', 'Av. Afonso Pena', 'Centro', '12345', '79032500', 'ATIVO',  4156, 30);

insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values (2, 'Nu Pagamentos S.A.', '18236120000158', 'Av. Mato Grosso', 'Centro', '538', '79010220', 'ATIVO', 4156, 30);

insert into comum_siga.tb_pessoa (pe_id, pe_nome_razaosocial, pe_cpf_cnpj, pe_logradouro, pe_bairro, pe_numero, pe_cep, pe_situacao, mu_id, pa_id)
values (3, 'Jumbitos Comercio de Alimentos Ltda', '01382349000106', 'Rua Bahia', 'São Francisco', '979', '79010180', 'ATIVO', 4156, 30);

ALTER SEQUENCE comum_siga.seq_pessoa INCREMENT BY 3;
SELECT comum_siga.seq_pessoa.nextval FROM DUAL;
ALTER SEQUENCE comum_siga.seq_pessoa INCREMENT BY 1;

-- ATRIBUTOS DOS FORNECEDORES
insert into comum_siga.tb_pessoa_atributo (pa_id, pa_atributo, pa_tipo_atributo, pa_valor, pe_id)
values (COMUM_SIGA.SEQ_PESSOA_ATRIBUTO.NEXTVAL, 'EMAIL', 'EMAIL', 'fornecedor1@azi.com.br', 1);

insert into comum_siga.tb_pessoa_atributo (pa_id, pa_atributo, pa_tipo_atributo, pa_valor, pe_id)
values (COMUM_SIGA.SEQ_PESSOA_ATRIBUTO.NEXTVAL, 'EMAIL', 'EMAIL', 'fornecedor2@azi.com.br', 2);

insert into comum_siga.tb_pessoa_atributo (pa_id, pa_atributo, pa_tipo_atributo, pa_valor, pe_id)
values (COMUM_SIGA.SEQ_PESSOA_ATRIBUTO.NEXTVAL, 'EMAIL', 'EMAIL', 'fornecedor3@azi.com.br', 3);


insert into comum_siga.tb_pessoa_atributo (pa_id, pa_atributo, pa_tipo_atributo, pa_valor, pe_id)
values (COMUM_SIGA.SEQ_PESSOA_ATRIBUTO.NEXTVAL, 'TELEFONE', 'TELEFONE_FIXO_COMERCIAL', '30270001', 1);

insert into comum_siga.tb_pessoa_atributo (pa_id, pa_atributo, pa_tipo_atributo, pa_valor, pe_id)
values (COMUM_SIGA.SEQ_PESSOA_ATRIBUTO.NEXTVAL, 'TELEFONE', 'TELEFONE_FIXO_COMERCIAL', '30215050', 2);

insert into comum_siga.tb_pessoa_atributo (pa_id, pa_atributo, pa_tipo_atributo, pa_valor, pe_id)
values (COMUM_SIGA.SEQ_PESSOA_ATRIBUTO.NEXTVAL, 'TELEFONE', 'TELEFONE_FIXO_COMERCIAL', '99186117', 3);

-- USUARIOS
insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values (COMUM.SEQ_USUARIO.NEXTVAL, 'fornecedor1', 'fornecedor1@azi.com.br', '57708514738', 'Fornecedor Google', 0, '33266655', 'ATIVO', 1, 30, 'EXTERNO');

insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values (COMUM.SEQ_USUARIO.NEXTVAL, 'fornecedor2', 'fornecedor2@azi.com.br', '90787358215', 'Fornecedor Nu Bank', 0, '99814242', 'ATIVO', 1, 30, 'EXTERNO');

insert into comum.tb_usuario (us_id, us_email, us_email_contato, us_doc_chave, us_nome, us_exibir_representacoes, us_telefone, us_situacao, id_id, pa_id, us_tipo_usuario)
values (COMUM.SEQ_USUARIO.NEXTVAL, 'fornecedor3', 'fornecedor3@azi.com.br', '93548959059', 'Fornecedor Jumbitos', 0, '33514332', 'ATIVO', 1, 30, 'EXTERNO');

insert into hal.tb_usuario_hal (us_id, us_senha)
values ((select us_id from comum.tb_usuario where us_email = 'fornecedor1'), 'UTr6YlcHzuQ=' );

insert into hal.tb_usuario_hal (us_id, us_senha)
values ((select us_id from comum.tb_usuario where us_email = 'fornecedor2'), 'UTr6YlcHzuQ=' );

insert into hal.tb_usuario_hal (us_id, us_senha)
values ((select us_id from comum.tb_usuario where us_email = 'fornecedor3'), 'UTr6YlcHzuQ=' );

-- DOMINIO (LICITANTES X FORNECEDORES DO COMPRAS)

    -- Vincula licitantes aos fornecedores
insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
values (HAL.SEQ_DOMINIO.NEXTVAL, 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '06990590000123'), (select us_id from comum.tb_usuario where us_email = 'fornecedor1'));

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
values (HAL.SEQ_DOMINIO.NEXTVAL, 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158'), (select us_id from comum.tb_usuario where us_email = 'fornecedor1'));

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
values (HAL.SEQ_DOMINIO.NEXTVAL, 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106'), (select us_id from comum.tb_usuario where us_email = 'fornecedor1'));

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
values (HAL.SEQ_DOMINIO.NEXTVAL, 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158'), (select us_id from comum.tb_usuario where us_email = 'fornecedor2'));

insert into hal.tb_dominio (dm_id, dm_tipo_cliente, dm_id_cliente, us_id)
values (HAL.SEQ_DOMINIO.NEXTVAL, 'DEFAULT', (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106'), (select us_id from comum.tb_usuario where us_email = 'fornecedor3'));

-- -- DETALHES EFCAZ
-- insert into efcaz.tb_pessoa_efcaz(pe_id, pe_emissao_cerca, nj_id, po_id)
-- values(1, 1, 1, 1);
--
-- insert into efcaz.tb_pessoa_efcaz(pe_id, pe_emissao_cerca, nj_id, po_id)
-- values(2, 1, 1, 1);
--
-- insert into efcaz.tb_pessoa_efcaz(pe_id, pe_emissao_cerca, nj_id, po_id)
-- values(3, 1, 1, 2);
--
-- -- DOMÍNIO PERFIL FORNECEDORES
--
--     --Vincula dominio DEFAULT ao perfil 'Edital'
-- insert into hal.tb_dominio_perfil (dm_id, pf_id)
-- values((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor1') and dm_id_cliente = (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '06990590000123')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1));
--
-- insert into hal.tb_dominio_perfil (dm_id, pf_id)
-- values((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor1') and dm_id_cliente = (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1));
--
-- insert into hal.tb_dominio_perfil (dm_id, pf_id)
-- values((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor1') and dm_id_cliente = (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1));
--
-- insert into hal.tb_dominio_perfil (dm_id, pf_id)
-- values((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor2')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1));
--
-- insert into hal.tb_dominio_perfil (dm_id, pf_id)
-- values((select dm_id from hal.tb_dominio where dm_tipo_cliente = 'DEFAULT' and us_id = (select us_id from comum.tb_usuario where us_email = 'fornecedor3')), (select pf_id from comum.tb_perfil where pf_nome = 'Edital' and pr_id = 1));
--
--
--
-- -- Vincula usuário a uma empresa (tb_usuario_fornecedor no Compras).
--
-- insert into efcaz.tb_representante (re_id, us_id, pe_id, re_situacao)
-- values(
--   EFCAZ.SEQ_REPRESENTANTE.NEXTVAL,
--   (select us_id from comum.tb_usuario where us_email = 'fornecedor1'),
--   (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '06990590000123'),
--   'ATIVO'
-- );
--
-- insert into efcaz.tb_representante (re_id, us_id, pe_id, re_situacao)
-- values(
--   EFCAZ.SEQ_REPRESENTANTE.NEXTVAL,
--   (select us_id from comum.tb_usuario where us_email = 'fornecedor1'),
--   (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158'),
--   'ATIVO'
-- );
--
-- insert into efcaz.tb_representante (re_id, us_id, pe_id, re_situacao)
-- values(
--   EFCAZ.SEQ_REPRESENTANTE.NEXTVAL,
--   (select us_id from comum.tb_usuario where us_email = 'fornecedor1'),
--   (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106'),
--   'ATIVO'
-- );
--
-- insert into efcaz.tb_representante (re_id, us_id, pe_id, re_situacao)
-- values(
--   EFCAZ.SEQ_REPRESENTANTE.NEXTVAL,
--   (select us_id from comum.tb_usuario where us_email = 'fornecedor2'),
--   (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '18236120000158'),
--   'ATIVO'
-- );
--
-- insert into efcaz.tb_representante (re_id, us_id, pe_id, re_situacao)
-- values(
--   EFCAZ.SEQ_REPRESENTANTE.NEXTVAL,
--   (select us_id from comum.tb_usuario where us_email = 'fornecedor3'),
--   (select pe_id from comum_siga.tb_pessoa where pe_cpf_cnpj = '01382349000106'),
--   'ATIVO'
-- );
--
select * from DUAL;
