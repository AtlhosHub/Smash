insert into usuario
    (nome, email, senha, celular, data_nascimento, deletado)
values
    ('User', 'user@adm.com', '$2a$10$UM8lVJYL2yz5nhvlcD6Oh.vQkGEl/klH..96PzoVwd3HYXzvD33k.', '11999999999', '1990-01-01', 0),
    ('Alex', 'alex.barreira@sptech.com', '$2a$10$/Ks/lsRMn7nhFDUtjrIVluZlyRsVqXYdX9aqCn74j0MvPESyUtQHG', '11999999999', '1990-01-01', 0);

insert into valor_mensalidade
    (data_inclusao, valor, desconto, manual)
values
    (now(), 120.00, false, false);

insert into horario_pref
    (horario_aula_inicio, horario_aula_fim, data_inclusao)
values
    ('18:00', '20:00', now()),
    ('20:00', '22:00', now()),
    ('14:00', '17:00', now());