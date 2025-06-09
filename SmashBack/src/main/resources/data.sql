insert into usuario
    (nome, email, senha, celular, data_nascimento, deletado)
values
    ('User', 'user@adm.com', '$2a$10$UM8lVJYL2yz5nhvlcD6Oh.vQkGEl/klH..96PzoVwd3HYXzvD33k.', '(11) 99999-9999', '1990-01-01', 0);

insert into valor_mensalidade
    (data_inclusao, valor, desconto, manual)
values
    (now(), 120.00, false, false);

insert into horario_pref
    (horario_aula_inicio, horario_aula_fim, data_inclusao)
values
    ('14:00', '17:00', now()),
    ('18:00', '20:00', now()),
    ('20:00', '22:00', now());

insert into usuario
    (nome, email, senha, celular, data_nascimento, genero, deletado, usuario_inclusao_id, data_inclusao)
values
    ('Walter', 'walter@acdnb.com', '$2a$10$UM8lVJYL2yz5nhvlcD6Oh.vQkGEl/klH..96PzoVwd3HYXzvD33k.', '(11) 98380-6989', '1969-03-02', 'Masculino', false, 1, now()),
    ('Lucas Militão', 'militao@acdnb.com', '$2a$10$UM8lVJYL2yz5nhvlcD6Oh.vQkGEl/klH..96PzoVwd3HYXzvD33k.', '(11) 98380-6989', '1995-01-12', 'Masculino', false, 1, now()),
    ('Akira', 'akira@acdnb.com', '$2a$10$UM8lVJYL2yz5nhvlcD6Oh.vQkGEl/klH..96PzoVwd3HYXzvD33k.', '(11) 98380-6989', '1974-12-11', 'Masculino', false, 1, now()),
    ('Paulo', 'paulo@acdnb.com', '$2a$10$UM8lVJYL2yz5nhvlcD6Oh.vQkGEl/klH..96PzoVwd3HYXzvD33k.', '(11) 98380-6989', '1965-04-10', 'Masculino', false, 1, now());

-- Massa para apresentação
insert into lista_espera
    (nome, email, celular, data_nascimento, genero, data_interesse, horario_pref_id, usuario_inclusao_id, data_inclusao)
values
    ('Theo Daniel Ferreira', 'theo.ferreira@gmail.com', '(11) 98234-2127', '1998-05-15', 'Masculino', '2025-05-18', 1, 1, '2025-05-18');

INSERT INTO endereco 
    (logradouro, num_logradouro, bairro, cidade, estado, cep)
VALUES
    ('Avenida Maria Dias', 776, 'Jardim São Carlos', 'Bebedouro', 'SP', '14702-248'),
    ('Rua Capitão João Carlos', 864, 'Nossa Senhora do Ó', 'São Paulo', 'SP', '02926-060'),
    ('Rua Vitório', 288, 'Jardim Julieta', 'Itapevi', 'SP', '06653-400'),
    ('Rua Felício Atala', 457, 'Jardim Flórida', 'Bauru', 'SP', '17024-650'),
    ('Rua Odilon Chaves', 742, 'Jardim Nazareth', 'São Paulo', 'SP', '08150-560'),
    ('Avenida Maria Lopes Castilho', 952, 'Vila Espírito Santo', 'Sorocaba', 'SP', '18051-410'),
    ('Rua Rio dos Cedros', 849, 'Jardim Peri', 'São Paulo', 'SP', '02679-060'),
    ('Rua Joaquim Matos', 113, 'Vila Mangalot', 'São Paulo', 'SP', '05131-010'),
    ('Rua Antônio Fernando Arruda Moraes', 670, 'Vila Arruda', 'Itapetininga', 'SP', '18212-110'),
    ('Rua João Batista Carri', 949, 'Parque Residencial Maria Stella Faga', 'São Carlos', 'SP', '13568-410');

INSERT INTO aluno 
    (nome, email, data_nascimento, cpf, rg, genero, celular, nacionalidade, naturalidade, telefone, profissao, ativo, tem_atestado, deficiencia, autorizado, endereco_id, data_inclusao, usuario_inclusao_id)
VALUES
    ('Giovanna Julia Assis', 'giovanna-assis81@gmail.com', '1979-06-09', '14389008803', '10.036.757-4', 'Feminino', '(17) 98171-3456', 'Brasileira', 'Bebedouro', null, 'Engenheira de Software', true, true, 'Daltonismo', true, 1, '2025-06-01 09:00:00', 1),
    ('Yuri Enrico Thales Duarte', 'yuri_duarte@gmail.com', '1989-03-17', '08582254849', '11.874.451-3', 'Masculino', '(11) 98755-1988', 'Brasileira', 'São Paulo', '(11) 2635-2938', 'Técnico de Enfermagem', true, true, null, true, 2, '2025-06-01 09:00:00', 1),
    ('Lucca Raimundo dos Santos', 'lucca.raimundo.dossantos@outlook.com', '1999-06-02', '08499656838', '26.150.856-8', 'Masculino', '(11) 99574-1639', 'Brasileira', 'Itapevi', '(11) 2783-6298', 'Marceneiro', true, true, null, true, 3, '2025-06-01 09:00:00', 1),
    ('Sérgio Manuel Márcio da Mata', 'sergio_damata@hotmail.com', '1997-08-25', '18053949835', '48.206.398-1', 'Masculino', '(14) 99518-6976', 'Brasileira', 'Bauru', '(14) 3630-6113', 'Estagiário', true, true, null, true, 4, '2025-06-01 09:00:00', 1),
    ('Carlos Eduardo Iago Ramos', 'carloseduardoramos@outlook.com', '1960-07-02', '07195674835', '15.526.220-8', 'Masculino', '(11) 99788-5434', 'Brasileira', 'São Paulo', null, 'Designer Gráfico', true, true,'Perda auditiva parcial', true, 5, '2025-06-01 09:00:00', 1),
    ('Bruna Stefany Almeida', 'bruna.stefany.almeida@gmail.com', '1990-02-06', '63192900806', '48.688.760-1', 'Feminino', '(15) 98726-7162', 'Brasileira', 'Sorocaba', '(15) 2625-4607', 'Pintora', true, true, null, true, 6, '2025-06-01 09:00:00', 1),
    ('Osvaldo Joaquim Julio Lopes', 'osvaldojoaquimlopes@hotmail.com', '2002-06-06', '58373390863', '22.492.238-5', 'Masculino', '(11) 98748-1114', 'Brasileira', 'São Paulo', '(11) 2534-6517', 'Professor', true, true, 'Baixa visão', true, 7, '2025-06-01 09:00:00', 1),
    ('Lorena Rebeca Eliane Monteiro', 'lorena.rebeca.monteiro@hotmail.com', '1967-04-07', '39173602841', '33.813.771-3', 'Feminino', '(11) 98619-7210', 'Brasileira', 'São Paulo', '(11) 2653-5312', 'Arquiteta', true, true, null, true, 8, '2025-06-01 09:00:00', 1),
    ('Julio Thomas Peixoto', 'julio_thomas_peixoto@gmail.com', '1974-06-12', '38544057829', '27.755.336-2', 'Masculino', '(15) 99812-7129', 'Brasileira', 'Itapetininga', '(15) 3610-5532', 'Chef de Cozinha', true, true, null, true, 9, '2025-06-01 09:00:00', 1),
    ('Samuel Martin Fogaça', 'samuel.martin.fogaca@outlook.com', '1965-03-26', '39023853830', '42.596.317-2', 'Masculino', '(16) 99122-7178', 'Brasileira', 'São Carlos', null, 'Analista de Dados', true, true, null, true, 10, '2025-06-01 09:00:00', 1);

-- Aluno ID 1
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (1, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (1, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (1, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (1, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (1, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (1, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 2
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (2, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (2, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (2, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (2, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (2, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (2, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 3
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (3, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (3, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (3, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (3, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (3, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (3, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 4
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (4, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (4, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (4, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (4, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (4, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (4, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 5
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (5, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (5, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (5, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (5, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (5, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (5, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 6
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (6, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (6, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (6, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (6, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (6, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (6, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 7
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (7, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (7, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (7, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (7, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (7, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (7, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 8
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (8, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (8, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (8, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (8, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (8, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (8, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 9
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (9, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (9, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (9, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (9, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (9, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (9, 1, '2025-11-12', 'PENDENTE', false);

-- Aluno ID 10
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (10, 1, '2025-06-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (10, 1, '2025-07-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (10, 1, '2025-08-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (10, 1, '2025-09-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (10, 1, '2025-10-12', 'PENDENTE', false);
INSERT INTO mensalidade (aluno_id, valor_mensalidade_id, data_vencimento, status, automatica) VALUES (10, 1, '2025-11-12', 'PENDENTE', false);


insert into valor_mensalidade
    (data_inclusao, valor, desconto, manual)
values
    (now(), 110.00, true, false);

UPDATE mensalidade
SET data_pagamento = '2025-06-01 10:00:00', status = 'PAGO', forma_pagamento = 'Pix', automatica = true, valor_mensalidade_id = 2
WHERE aluno_id in (1,2) AND data_vencimento = '2025-06-12';

UPDATE mensalidade
SET data_pagamento = '2025-06-09 00:00:00', status = 'PAGO', forma_pagamento = 'Pix', automatica = true
WHERE aluno_id in (3,5,7,8,10) AND data_vencimento = '2025-06-12';