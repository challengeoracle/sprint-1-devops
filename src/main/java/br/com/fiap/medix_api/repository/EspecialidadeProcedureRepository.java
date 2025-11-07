package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EspecialidadeProcedureRepository extends JpaRepository<Especialidade, Long> {

    // INSERT: Chama PROC_INSERT_ESPECIALIDADE(p_nome)
    // Nota: O PL/SQL gerará o ID.
    @Modifying
    @Transactional
    @Query(value = "CALL PROC_INSERT_ESPECIALIDADE(:nome)", nativeQuery = true)
    void callInsertEspecialidade(@Param("nome") String nome);

    // UPDATE: Chama PROC_UPDATE_ESPECIALIDADE(p_id, p_nome)
    @Modifying
    @Transactional
    @Query(value = "CALL PROC_UPDATE_ESPECIALIDADE(:id, :nome)", nativeQuery = true)
    void callUpdateEspecialidade(@Param("id") Long id, @Param("nome") String nome);

    // DELETE: Chama PROC_DELETE_ESPECIALIDADE(p_id) (DELETE FÍSICO)
    @Modifying
    @Transactional
    @Query(value = "CALL PROC_DELETE_ESPECIALIDADE(:id)", nativeQuery = true)
    void callDeleteEspecialidade(@Param("id") Long id);
}