package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.request.AtualizarAvaliacaoDto;
import br.com.fiap.medix_api.dto.request.CadastrarAvaliacaoDto;
import br.com.fiap.medix_api.model.Avaliacao;
import br.com.fiap.medix_api.repository.AvaliacaoProcedureRepository;
import br.com.fiap.medix_api.repository.AvaliacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class AvaliacaoService {

    private AvaliacaoRepository avaliacaoRepository;
    private final AvaliacaoProcedureRepository avaliacaoProcedureRepository;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    // --- C - CREATE (via PROCEDURE) ---
    @Transactional
    public Avaliacao registrarAvaliacao(CadastrarAvaliacaoDto dto) {
        LocalTime horarioAvaliacao = LocalTime.parse(dto.getHorario(), timeFormatter);

        // CHAMA A PROCEDURE ORACLE PARA INSERT
        avaliacaoProcedureRepository.callInsertAvaliacao(
                horarioAvaliacao,
                dto.getSetor(),
                dto.getLocal(),
                dto.getAvaliacao()
        );

        // Retorna um placeholder (como a procedure não devolve o ID, simulamos o objeto de retorno)
        Avaliacao placeholder = new Avaliacao();
        placeholder.setHorarioAvaliacao(horarioAvaliacao);
        placeholder.setSetor(dto.getSetor());
        placeholder.setLocal(dto.getLocal());
        placeholder.setAvaliacao(dto.getAvaliacao());
        return placeholder;
    }

    // --- MÉTODOS AUXILIARES PARA DEMONSTRAÇÃO (2x CUD) ---
    @Transactional
    public void demoInsert(String setor, String local, String avaliacao) {
        // Método de demonstração para INSERT com valores fixos
        LocalTime horario = LocalTime.now().withNano(0);
        avaliacaoProcedureRepository.callInsertAvaliacao(horario, setor, local, avaliacao);
    }

    @Transactional
    public void demoUpdate(Long id, String novoSetor) {
        // Método de demonstração para UPDATE, atualizando apenas o Setor
        Avaliacao avaliacaoAtual = this.buscarPorId(id);

        // Chamada da Procedure. Passa o valor novo para Setor, e mantém os valores antigos para Local e Avaliacao
        avaliacaoProcedureRepository.callUpdateAvaliacao(
                id,
                novoSetor,
                avaliacaoAtual.getLocal(),
                avaliacaoAtual.getAvaliacao()
        );
    }

    @Transactional
    public void demoDelete(Long id) {
        // Método de demonstração para DELETE LÓGICO
        this.buscarPorId(id);
        avaliacaoProcedureRepository.callDeleteAvaliacao(id); // Soft Delete via Procedure
    }

    // --- R - READ (Buscas normais) ---
    public List<Avaliacao> listar(String status) {
        if ("deletado".equalsIgnoreCase(status)) {
            return avaliacaoRepository.findAllByDeletedIsOne();
        }
        return avaliacaoRepository.findAllByDeletedIsZero();
    }

    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findByIdAndDeletedIs(id, 0)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada ou inativa com o ID: " + id));
    }


    // --- U - UPDATE (via JPA) ---
    @Transactional
    public Avaliacao atualizar(Long id, AtualizarAvaliacaoDto dto) {
        Avaliacao avaliacaoAtual = this.buscarPorId(id);

        // Lógica de update normal, usando a procedure se necessário
        avaliacaoProcedureRepository.callUpdateAvaliacao(
                id,
                dto.getSetor() != null ? dto.getSetor() : avaliacaoAtual.getSetor(),
                dto.getLocal() != null ? dto.getLocal() : avaliacaoAtual.getLocal(),
                dto.getAvaliacao() != null ? dto.getAvaliacao() : avaliacaoAtual.getAvaliacao()
        );

        return this.buscarPorId(id);
    }

    // --- D - DELETE (via PROCEDURE) ---
    @Transactional
    public void excluir(Long id) {
        this.buscarPorId(id);
        avaliacaoProcedureRepository.callDeleteAvaliacao(id);
    }
}