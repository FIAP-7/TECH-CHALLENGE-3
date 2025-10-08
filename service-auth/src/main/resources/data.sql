insert into public.role (id, name) values (0, 'ADMIN');
insert into public.role (id, name) values (1, 'MEDICO');
insert into public.role (id, name) values (2, 'ENFERMERO');
insert into public.role (id, name) values (3, 'PACIENTE');

insert into public.usuario (active, role_id, password, username, name, email)
values (
  true,
  (select id from public.role where name = 'ADMIN'),
  '$2a$10$le9yY1g8ilNcr.OdtqVspOrJR6XrdFxeZbonrdZAmhq40LeGRi0XO',
  'admin',
  'Administrador do Sistema',
  'adm@teste.com'
);