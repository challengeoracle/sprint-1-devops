-- ============================================================================
-- MASSA DE DADOS INICIAL PARA TESTES (DESENVOLVIMENTO)
-- ============================================================================

-- 1. UNIDADE DE SAÚDE (ID 1)
INSERT INTO TB_MEDI_UNIDADE_SAUDE (id_unidade_saude, deleted, dt_criacao, dt_atualizacao, nm_unidade, nr_cnpj, ds_endereco, nr_telefone, tp_unidade)
VALUES (1, 0, NOW(), NOW(), 'Hospital Central (TESTE)', '11111111000111', 'Av. Paulista, 1000', '11999998888', 'HOSPITAL_GERAL');

-- 2. SALA / CONSULTÓRIO (ID 1)
INSERT INTO TB_MEDI_SALA (id_sala, deleted, nm_sala, ds_tipo_sala, id_unidade_saude)
VALUES (1, 0, 'Consultório 101 (Cardio)', 'CONSULTORIO', 1);

-- 3. ESPECIALIDADES (Necessário para o fluxo de disponibilidade)
INSERT INTO TB_MEDI_ESPECIALIDADE (id_especialidade, nm_especialidade) VALUES (1, 'Cardiologia');
INSERT INTO TB_MEDI_ESPECIALIDADE (id_especialidade, nm_especialidade) VALUES (2, 'Ortopedia');
INSERT INTO TB_MEDI_ESPECIALIDADE (id_especialidade, nm_especialidade) VALUES (3, 'Clínica Geral');

-- ============================================================================
-- USUÁRIOS (Senha padrão: 'senha123')
-- Hash BCrypt para 'senha123': $2a$10$6Ra0FhM5OIJ2RG8tfx3tWOtm0meVQsYbYr5CHra45/PMZkUGDMYN6
-- ============================================================================

-- 4. COLABORADOR (ID 1) - Dr. Silva (Cardiologista)
INSERT INTO TB_MEDI_USUARIO (id_usuario, deleted, dt_criacao, dt_atualizacao, nm_usuario, ds_email, ds_senha_hash, nr_cpf, tp_usuario)
VALUES (1, 0, NOW(), NOW(), 'Dr. Silva (Admin)', 'admin@medix.com', '$2a$10$6Ra0FhM5OIJ2RG8tfx3tWOtm0meVQsYbYr5CHra45/PMZkUGDMYN6', '12345678901', 'COLABORADOR');

-- Vincula o colaborador à Unidade 1 e Especialidade 1 (Cardiologia)
INSERT INTO TB_MEDI_COLABORADOR (id_usuario, id_unidade_saude, id_especialidade, ds_cargo, nr_registro_profissional, dt_admissao)
VALUES (1, 1, 1, 'Médico Cardiologista', 'CRM/SP 123456', '2023-01-10');

-- 5. PACIENTE (ID 2)
INSERT INTO TB_MEDI_USUARIO (id_usuario, deleted, dt_criacao, dt_atualizacao, nm_usuario, ds_email, ds_senha_hash, nr_cpf, tp_usuario)
VALUES (2, 0, NOW(), NOW(), 'Paciente Exemplo', 'paciente@medix.com', '$2a$10$6Ra0FhM5OIJ2RG8tfx3tWOtm0meVQsYbYr5CHra45/PMZkUGDMYN6', '99999999999', 'PACIENTE');

INSERT INTO TB_MEDI_PACIENTE (id_usuario, dt_nascimento, tp_sanguineo, ds_genero)
VALUES (2, '1995-05-20', 'O_POSITIVO', 'MASCULINO');

-- ============================================================================
-- AJUSTE DE SEQUÊNCIAS (Evita erros ao criar novos registros)
-- ============================================================================
ALTER TABLE TB_MEDI_UNIDADE_SAUDE ALTER COLUMN id_unidade_saude RESTART WITH 2;
ALTER TABLE TB_MEDI_SALA ALTER COLUMN id_sala RESTART WITH 2;
ALTER TABLE TB_MEDI_ESPECIALIDADE ALTER COLUMN id_especialidade RESTART WITH 4;
ALTER TABLE TB_MEDI_USUARIO ALTER COLUMN id_usuario RESTART WITH 3;