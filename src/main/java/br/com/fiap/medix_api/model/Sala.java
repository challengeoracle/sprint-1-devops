package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "TB_MEDI_SALA")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Soft Delete via Hibernate (opcional, mas recomendado para manter padrão)
@SQLDelete(sql = "UPDATE TB_MEDI_SALA SET deleted = 1 WHERE id_sala = ?")
@Where(clause = "deleted = 0")
public class Sala {

    @Id
    // ATUALIZADO: De IDENTITY para SEQUENCE
    @SequenceGenerator(name = "seq_sala", sequenceName = "SQ_MEDI_SALA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sala")
    @Column(name = "id_sala")
    private Long id;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @NotBlank(message = "O nome/número da sala é obrigatório.")
    @Size(max = 50)
    @Column(name = "nm_sala", nullable = false, length = 50)
    private String nome;

    @Size(max = 100)
    @Column(name = "ds_tipo_sala", length = 100)
    private String tipo; // Ex: "Consultório", "Raio-X"

    @NotNull(message = "A unidade de saúde é obrigatória.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidade_saude", nullable = false)
    private UnidadeSaude unidadeSaude;
}