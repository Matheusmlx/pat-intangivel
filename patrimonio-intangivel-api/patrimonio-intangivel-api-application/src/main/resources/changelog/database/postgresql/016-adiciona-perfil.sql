-- RETROATIVO --
insert into comum.tb_perfil(pf_id,pf_nome,pf_descricao,pf_tipo,pr_id,pf_dthr_cadastro,pf_usuario_cadastro)
values (nextval('comum.seq_perfil'),'Retroativo','Acesso ao módulo de patrimônio com permissão retroativa','INTERNO',401,current_timestamp(6),'Sistema');

insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Patrimonio.Retroativo'),(select pf_id from comum.tb_perfil where pf_nome = 'Retroativo' and pr_id = 401), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Patrimonio.Consulta'),(select pf_id from comum.tb_perfil where pf_nome = 'Retroativo' and pr_id = 401), 1,'S');
insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Patrimonio.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Retroativo' and pr_id = 401), 1,'S');
