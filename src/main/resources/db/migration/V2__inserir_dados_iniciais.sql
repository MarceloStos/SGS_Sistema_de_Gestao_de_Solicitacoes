-- 1. Inserindo Categorias (Mantidas as 5 originais)
INSERT INTO categoria (nome) VALUES
    ('Serviços'),
    ('Material'),
    ('Transporte'),
    ('Alimentação'),
    ('Outros');

-- 2. Inserindo Solicitantes (5 originais + 2 novos)
INSERT INTO solicitante (nome, cpf_cnpj) VALUES
    ('Marcelo Santos', '001.002.003-45'),
    ('João Silva', '111.111.111-11'),
    ('Tech Solutions SA', '22.222.222/0001-22'),
    ('Maria Oliveira', '222.222.222-22'),
    ('Logística Express', '33.333.333/0001-33'),
    ('Ana Costa', '444.444.444-44'),
    ('Gama Construtora', '55.555.555/0001-55');

-- 3. Inserindo Solicitações (Uma para cada status + algumas extras para dar volume)
INSERT INTO solicitacao (solicitante_id, categoria_id, descricao, valor, data_solicitacao, status) VALUES
-- Status: SOLICITADO (Para testar o botão "Liberar" ou "Rejeitar")
(1, 1, 'Manutenção preventiva de servidores', 1500.00, '2026-05-12 09:30:00', 'SOLICITADO'),
(6, 2, 'Compra de licenças de software', 1200.00, '2026-05-11 14:00:00', 'SOLICITADO'),

-- Status: LIBERADO (Para testar o botão "Aprovar" ou "Rejeitar")
(2, 2, 'Aquisição de suprimentos de escritório', 450.50, '2026-05-10 11:15:00', 'LIBERADO'),

-- Status: APROVADO (Para testar o botão "Cancelar")
(3, 3, 'Frete de equipamentos para a filial', 3200.00, '2026-05-09 16:45:00', 'APROVADO'),

-- Status: REJEITADO (Status final, testar se os botões somem/ficam bloqueados)
(4, 4, 'Coffee break para evento corporativo', 850.00, '2026-05-05 10:00:00', 'REJEITADO'),

-- Status: CANCELADO (Status final, testar se os botões somem/ficam bloqueados)
(5, 1, 'Consultoria técnica externa', 5000.00, '2026-05-01 08:20:00', 'CANCELADO'),

-- Mais uma recente apenas para compor a tabela
(7, 5, 'Treinamento de segurança do trabalho', 2500.00, '2026-05-12 10:30:00', 'SOLICITADO');