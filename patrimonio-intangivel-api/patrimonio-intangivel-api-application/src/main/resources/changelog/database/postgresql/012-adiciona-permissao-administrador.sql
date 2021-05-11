insert into hal.tb_permissao_perfil(fu_id, pf_id, pl_id, pp_permissao)
values ((select fu_id from hal.tb_funcao where fu_nome like 'Patrimonio.Normal'),(select pf_id from comum.tb_perfil where pf_nome = 'Administrador' and pr_id = 401), 1,'S');
