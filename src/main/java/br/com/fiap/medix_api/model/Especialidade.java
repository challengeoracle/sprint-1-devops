package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "TB_MEDI_ESPECIALIDADE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidade extends RepresentationModel<Especialidade> {

    @Id
    // ATUALIZADO: De IDENTITY para SEQUENCE
    @SequenceGenerator(name = "seq_especialidade", sequenceName = "SQ_MEDI_ESPECIALIDADE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_especialidade")
    @Column(name = "id_especialidade")
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nm_especialidade", nullable = false, unique = true, length = 50)
    private String nome;
}