package br.com.fiap.medix_api.model;

import br.com.fiap.medix_api.enums.StatusAgendamento;
import br.com.fiap.medix_api.enums.TipoAgendamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MEDI_AGENDAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    // ATUALIZADO: De IDENTITY para SEQUENCE
    @SequenceGenerator(name = "seq_agendamento", sequenceName = "SQ_MEDI_AGENDAMENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_agendamento")
    @Column(name = "id_agendamento")
    private Long id;

    @CreationTimestamp
    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "dt_atualizacao")
    private LocalDateTime dataAtualizacao;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_colaborador", nullable = false)
    private Colaborador colaborador;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_unidade_saude", nullable = false)
    private UnidadeSaude unidadeSaude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sala")
    private Sala sala;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    @Column(name = "dt_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "A data e hora de fim são obrigatórias.")
    @Column(name = "dt_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tp_agendamento", nullable = false, length = 30)
    private TipoAgendamento tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "st_agendamento", nullable = false, length = 30)
    private StatusAgendamento status;

    @Column(name = "ds_observacoes", length = 500)
    private String observacoes;
}