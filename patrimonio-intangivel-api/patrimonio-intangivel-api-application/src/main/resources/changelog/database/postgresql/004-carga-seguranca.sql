-- MODULOS E FUNCOES --

insert into comum.tb_modulo (mo_id,mo_nome,mo_descricao,pr_id,mo_dthr_cadastro,mo_usuario_cadastro)
values (nextval('comum.seq_modulo'),'Patrimonio','Modulo de acesso às telas de gerenciamento do Patrimonio', 401, current_timestamp(6),'Sistema');

insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Patrimonio.Normal','Função de acesso normal às telas de gerenciamento do Patrimonio', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Patrimonio.Consulta','Função de acesso de consulta às telas de gerenciamento do Patrimonio', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'Patrimonio.Retroativo','Função de acesso para cadastro de ativações retroativas', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

insert into comum.tb_modulo (mo_id,mo_nome,mo_descricao,pr_id,mo_dthr_cadastro,mo_usuario_cadastro)
values (nextval('comum.seq_modulo'),'Configurações','Modulo de acesso às telas de configurações do sistema', 401, current_timestamp(6),'Sistema');
insert into hal.tb_funcao (fu_id,fu_nome,fu_descricao,mo_id,pl_id,fu_dthr_cadastro,fu_usuario_cadastro)
values (nextval('hal.seq_funcao'),'ContaContabil.Normal','Função de acesso de edição a tela de gerenciamento da Conta Contábil', currval('comum.seq_modulo'), 1, current_timestamp(6),'Sistema');

-- PERFIS --

-- SERVIDOR --
insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Servidor','Servidor operacional de patrimonio','INTERNO',401,current_timestamp(6),'Sistema');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Patrimonio.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 401), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Patrimonio.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Servidor' and pr_id = 401), 1,'S');

-- CONSULTOR --
insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Consultor','Acesso de consulta ao patrimonio','INTERNO',401,current_timestamp(6),'Sistema');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Patrimonio.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Consultor' and pr_id = 401), 1,'S');

-- ADMINISTRADOR --
insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Administrador','Acesso administrativo ao patrimonio','INTERNO',401,current_timestamp(6),'Sistema');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'ContaContabil.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 401), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Patrimonio.Retroativo'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 401), 1,'S');
