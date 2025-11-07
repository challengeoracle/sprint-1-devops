package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.model.Especialidade;
import br.com.fiap.medix_api.repository.EspecialidadeProcedureRepository;
import br.com.fiap.medix_api.repository.EspecialidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    // CORREÇÃO: O campo deve ser privado.
    private final EspecialidadeRepository especialidadeRepository;
    private final EspecialidadeProcedureRepository especialidadeProcedureRepository;

    // NOVO MÉTODO: Expõe a funcionalidade findAll() para a Controller
    public List<Especialidade> listarTodas() {
        return especialidadeRepository.findAll();
    }

    // Método auxiliar para buscar por ID (mantendo o padrão)
    public Especialidade buscarPorId(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade não encontrada com ID: " + id));
    }

    // --- C - CREATE (via PROCEDURE) ---
    @Transactional
    public Especialidade criar(String nomeEspecialidade) {
        // CHAMA A PROCEDURE ORACLE PARA INSERT
        especialidadeProcedureRepository.callInsertEspecialidade(nomeEspecialidade);

        // A procedure não retorna o ID. Re-buscamos por nome (UNIQUE)
        return especialidadeRepository.findByNomeIgnoreCase(nomeEspecialidade)
                .orElseThrow(() -> new IllegalStateException("Especialidade criada, mas falha ao recuperar por nome."));
    }

    // --- U - UPDATE (via PROCEDURE) ---
    @Transactional
    public Especialidade atualizar(Long id, String novoNome) {
        // 1. Verifica se existe
        this.buscarPorId(id);

        // 2. CHAMA A PROCEDURE ORACLE PARA UPDATE
        especialidadeProcedureRepository.callUpdateEspecialidade(id, novoNome);

        // 3. Retorna o objeto atualizado
        return this.buscarPorId(id);
    }

    // --- D - DELETE (via PROCEDURE) ---
    @Transactional
    public void excluir(Long id) {
        // 1. Verifica se existe
        this.buscarPorId(id);

        // 2. CHAMA A PROCEDURE ORACLE PARA DELETE
        // Note que se houver FK, a procedure no Oracle deve tratar a exceção (RAISE_APPLICATION_ERROR)
        especialidadeProcedureRepository.callDeleteEspecialidade(id);
    }
}