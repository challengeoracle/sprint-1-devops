package br.com.fiap.medix_api.repository;

import br.com.fiap.medix_api.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

    // Listar todas as especialidades que têm pelo menos um médico ativo
    @Query("SELECT DISTINCT e FROM Colaborador c JOIN c.especialidade e WHERE c.deleted = 0")
    List<Especialidade> findAllComMedicosAtivos();
}