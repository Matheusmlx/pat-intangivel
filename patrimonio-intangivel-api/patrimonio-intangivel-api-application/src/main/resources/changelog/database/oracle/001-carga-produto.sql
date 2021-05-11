insert into comum.tb_produto (pr_id,pr_nome,pr_descricao,pr_titulo_img,pr_img_principal,pr_css_default,pl_id) values (401,'patrimonio-intangivel','Módulo de Patrimônio Intangível.','','','default',1);

ALTER SEQUENCE comum.seq_produto_atributo INCREMENT BY 1600;
SELECT comum.seq_produto_atributo.nextval FROM DUAL;
ALTER SEQUENCE comum.seq_produto_atributo INCREMENT BY 1;

insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginTipo','INTERNO',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginAreaInterno','Área do Servidor',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginChaveAcessoInterno','Usuário do Compras',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginFacebookInterno','false',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginEsqueceuSenhaInterno','true',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginAutoCadastroInterno','true',401);

insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginAreaExterno','Área do Fornecedor',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginChaveAcessoExterno','CPF ou Email',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginFacebookExterno','false',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginEsqueceuSenhaExterno','false',401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pr_id) values (comum.seq_produto_atributo.nextval,'loginAutoCadastroExterno','false',401);

insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (comum.seq_produto_atributo.nextval, 'logo', 'repo1:patrimoniointangivel/logo.svg', 'ARQUIVO', 401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (comum.seq_produto_atributo.nextval, 'logoEmail', 'repo1:patrimoniointangivel/logoEmail.png', 'ARQUIVO', 401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (comum.seq_produto_atributo.nextval, 'logoMenu', 'repo1:patrimoniointangivel/logoMenu.svg', 'ARQUIVO', 401);
insert into comum.tb_produto_atributo (pa_id, pa_atributo, pa_valor, pa_tipo, pr_id) values (comum.seq_produto_atributo.nextval, 'logoMenuRetraido', 'repo1:patrimoniointangivel/logoMenuRetraido.svg', 'ARQUIVO', 401);
