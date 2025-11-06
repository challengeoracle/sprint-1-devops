package br.com.fiap.medix_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MEDI_UNIDADE_SAUDE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeSaude extends RepresentationModel<UnidadeSaude> {

    @Id
    // ATUALIZADO: De IDENTITY para SEQUENCE
    @SequenceGenerator(name = "seq_unidade_saude", sequenceName = "SQ_MEDI_UNIDADE_SAUDE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_unidade_saude")
    @Column(name = "id_unidade_saude")
    private Long id;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @CreationTimestamp
    @Column(name = "dt_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "dt_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @NotNull(message = "O nome da unidade não pode ser nulo.")
    @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
    @Column(name = "nm_unidade", nullable = false, length = 100)
    private String nome;

    @NotNull(message = "O CNPJ não pode ser nulo.")
    @Size(max = 14, message = "O CNPJ deve ter 14 caracteres.")
    @Column(name = "nr_cnpj", nullable = false, unique = true, length = 14)
    private String cnpj;

    @NotNull(message = "O endereço não pode ser nulo.")
    @Size(max = 255, message = "O endereço não pode exceder 255 caracteres.")
    @Column(name = "ds_endereco", nullable = false, length = 255)
    private String endereco;

    @Size(max = 15, message = "O telefone não pode exceder 15 caracteres.")
    @Column(name = "nr_telefone", length = 15)
    private String telefone;

    @NotNull(message = "O tipo de unidade não pode ser nulo.")
    @Size(max = 20, message = "O tipo de unidade não pode exceder 20 caracteres.")
    @Column(name = "tp_unidade", nullable = false, length = 20)
    private String tipoUnidade;

    @PrePersist
    public void prePersist() {
        if (deleted == null) {
            deleted = 0;
        }
    }
}