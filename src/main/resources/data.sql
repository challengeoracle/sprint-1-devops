-- ============================================================================
-- MASSA DE DADOS INICIAL PARA TESTES (DESENVOLVIMENTO) - SINTAXE ORACLE
-- ============================================================================

-- 1. UNIDADE DE SAÚDE (Usando a Sequence SQ_MEDI_UNIDADE_SAUDE)
INSERT INTO TB_MEDI_UNIDADE_SAUDE (id_unidade_saude, deleted, dt_criacao, dt_atualizacao, nm_unidade, nr_cnpj, ds_endereco, nr_telefone, tp_unidade)
VALUES (SQ_MEDI_UNIDADE_SAUDE.NEXTVAL, 0, SYSTIMESTAMP, SYSTIMESTAMP, 'Hospital Central (TESTE)', '11111111000111', 'Av. Paulista, 1000', '11999998888', 'HOSPITAL_GERAL');

-- 2. SALA / CONSULTÓRIO (Usando a Sequence SQ_MEDI_SALA)
-- Assumindo que o ID 1 foi inserido acima.
INSERT INTO TB_MEDI_SALA (id_sala, deleted, nm_sala, ds_tipo_sala, id_unidade_saude)
VALUES (SQ_MEDI_SALA.NEXTVAL, 0, 'Consultório 101 (Cardio)', 'CONSULTORIO', 1);

-- 3. ESPECIALIDADES (Usando a Sequence SQ_MEDI_ESPECIALIDADE)
INSERT INTO TB_MEDI_ESPECIALIDADE (id_especialidade, nm_especialidade) VALUES (SQ_MEDI_ESPECIALIDADE.NEXTVAL, 'Cardiologia');
INSERT INTO TB_MEDI_ESPECIALIDADE (id_especialidade, nm_especialidade) VALUES (SQ_MEDI_ESPECIALIDADE.NEXTVAL, 'Ortopedia');
INSERT INTO TB_MEDI_ESPECIALIDADE (id_especialidade, nm_especialidade) VALUES (SQ_MEDI_ESPECIALIDADE.NEXTVAL, 'Clínica Geral');

-- ============================================================================
-- USUÁRIOS (Senha padrão: 'senha123')
-- Hash BCrypt para 'senha123': $2a$10$6Ra0FhM5OIJ2RG8tfx3tWOtm0meVQsYbYr5CHra45/PMZkUGDMYN6
-- ============================================================================

-- 4. COLABORADOR (Usando a Sequence SQ_MEDI_USUARIO)
-- (Assumindo que os IDs das especialidades/unidades são 1, 2, 3)
INSERT INTO TB_MEDI_USUARIO (id_usuario, deleted, dt_criacao, dt_atualizacao, nm_usuario, ds_email, ds_senha_hash, nr_cpf, tp_usuario)
VALUES (SQ_MEDI_USUARIO.NEXTVAL, 0, SYSTIMESTAMP, SYSTIMESTAMP, 'Dr. Silva (Admin)', 'admin@medix.com', '$2a$10$6Ra0FhM5OIJ2RG8tfx3tWOtm0meVQsYbYr5CHra45/PMZkUGDMYN6', '12345678901', 'COLABORADOR');

-- Vincula o colaborador à Unidade 1 e Especialidade 1 (Cardiologia)
-- (Assumindo que o usuário Dr. Silva recebeu o ID 1 da sequência)
INSERT INTO TB_MEDI_COLABORADOR (id_usuario, id_unidade_saude, id_especialidade, ds_cargo, nr_registro_profissional, dt_admissao)
VALUES (1, 1, 1, 'Médico Cardiologista', 'CRM/SP 123456', TO_DATE('2023-01-10', 'YYYY-MM-DD'));

-- 5. PACIENTE (Usando a Sequence SQ_MEDI_USUARIO)
INSERT INTO TB_MEDI_USUARIO (id_usuario, deleted, dt_criacao, dt_atualizacao, nm_usuario, ds_email, ds_senha_hash, nr_cpf, tp_usuario)
VALUES (SQ_MEDI_USUARIO.NEXTVAL, 0, SYSTIMESTAMP, SYSTIMESTAMP, 'Paciente Exemplo', 'paciente@medix.com', '$2a$10$6Ra0FhM5OIJ2RG8tfx3tWOtm0meVQsYbYr5CHra45/PMZkUGDMYN6', '99999999999', 'PACIENTE');

-- (Assumindo que o Paciente Exemplo recebeu o ID 2 da sequência)
INSERT INTO TB_MEDI_PACIENTE (id_usuario, dt_nascimento, tp_sanguineo, ds_genero)
VALUES (2, TO_DATE('1995-05-20', 'YYYY-MM-DD'), 'O_POSITIVO', 'MASCULINO');