package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime; // Adicionar este import

@Repository
public interface AvaliacaoProcedureRepository extends JpaRepository<Avaliacao, Long> {

    // INSERT: Chama PROC_INSERT_AVALIACAO(p_horario, p_setor, p_local, p_aval)
    @Modifying
    @Transactional
    @Query(value = "CALL PROC_INSERT_AVALIACAO(:horario, :setor, :local, :avaliacao)", nativeQuery = true)
    void callInsertAvaliacao(
            @Param("horario") LocalTime horario,
            @Param("setor") String setor,
            @Param("local") String local,
            @Param("avaliacao") String avaliacao
    );

    // UPDATE: Chama PROC_UPDATE_AVALIACAO(p_id, p_setor, p_local, p_aval)
    // Nota: O PL/SQL usa os parâmetros como estão e faz a alteração direta.
    @Modifying
    @Transactional
    @Query(value = "CALL PROC_UPDATE_AVALIACAO(:id, :setor, :local, :avaliacao)", nativeQuery = true)
    void callUpdateAvaliacao(
            @Param("id") Long id,
            @Param("setor") String setor,
            @Param("local") String local,
            @Param("avaliacao") String avaliacao
    );

    // DELETE: Chama PROC_DELETE_AVALIACAO(p_id) (Soft Delete)
    @Modifying
    @Transactional
    @Query(value = "CALL PROC_DELETE_AVALIACAO(:id)", nativeQuery = true)
    void callDeleteAvaliacao(@Param("id") Long id);
}