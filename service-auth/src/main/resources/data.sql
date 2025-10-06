insert into public.role (name) values ('ADMIN');
insert into public.role (name) values ('MEDICO');
insert into public.role (name) values ('ENFERMERO');

insert into public.usuario (active, role_id, password, username) values (true, 1, '$2a$10$le9yY1g8ilNcr.OdtqVspOrJR6XrdFxeZbonrdZAmhq40LeGRi0XO', 'admin');
