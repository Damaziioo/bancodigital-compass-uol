INSERT INTO contas (id, nome, saldo, data_criacao) VALUES
   (RANDOM_UUID(), 'João Silva', 1000.00, NOW()),
   (RANDOM_UUID(), 'Maria Souza', 2500.00, NOW()),
   (RANDOM_UUID(), 'Carlos Lima', 500.00, NOW());