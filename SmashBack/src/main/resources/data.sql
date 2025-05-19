insert into usuario
    (nome, email, senha, celular, data_nascimento, deletado)
values
    ('User', 'user@adm.com', '$2a$10$UM8lVJYL2yz5nhvlcD6Oh.vQkGEl/klH..96PzoVwd3HYXzvD33k.', '11999999999', '1990-01-01', 0);

insert into valor_mensalidade
    (data_inclusao, valor)
values
    (now(), 100.00);