package br.com.fiap.medix_api.model;

import br.com.fiap.medix_api.enums.Genero;
import br.com.fiap.medix_api.enums.TipoSanguineo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TB_MEDI_PACIENTE")
@PrimaryKeyJoinColumn(name = "id_usuario")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends Usuario {

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotNull(message = "O tipo sanguíneo não pode ser nulo.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tp_sanguineo", nullable = false, length = 20)
    private TipoSanguineo tipoSanguineo;

    @NotNull(message = "O gênero não pode ser nulo.")
    @Enumerated(EnumType.STRING)
    @Column(name = "ds_genero", nullable = false, length = 20)
    private Genero genero;

    @ElementCollection
    @CollectionTable(name = "TB_MEDI_PACIENTE_ALERGIAS", joinColumns = @JoinColumn(name = "id_paciente"))
    @Column(name = "ds_alergia")
    @Size(max = 100, message = "O nome da alergia não pode exceder 100 caracteres.")
    private List<String> alergias;
}