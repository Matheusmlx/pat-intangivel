insert into comum.tb_produto (pr_id,pr_nome,pr_descricao,pr_titulo_img,pr_img_principal,pr_css_default,pl_id) values (401,'patrimonio-intangivel','Módulo de Patrimônio Intangível.','','','default',1);

select setval('comum.seq_produto_atributo', 1600);

insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginTipo','INTERNO',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginAreaInterno','Área do Servidor',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginChaveAcessoInterno','Usuário do Compras',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginFacebookInterno','false',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginEsqueceuSenhaInterno','true',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginAutoCadastroInterno','true',401);

insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginAreaExterno','Área do Fornecedor',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginChaveAcessoExterno','CPF ou Email',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginFacebookExterno','false',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginEsqueceuSenhaExterno','false',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (nextval('comum.seq_produto_atributo'),'loginAutoCadastroExterno','false',401);

insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (nextval('comum.seq_produto_atributo'), 'logo', 'repo1:patrimoniointangivel/logo.svg', 'ARQUIVO', 401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (nextval('comum.seq_produto_atributo'), 'logoEmail', 'repo1:patrimoniointangivel/logoEmail.png', 'ARQUIVO', 401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (nextval('comum.seq_produto_atributo'), 'logoMenu', 'repo1:patrimoniointangivel/logoMenu.svg', 'ARQUIVO', 401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (nextval('comum.seq_produto_atributo'), 'logoMenuRetraido', 'repo1:patrimoniointangivel/logoMenuRetraido.svg', 'ARQUIVO', 401);
