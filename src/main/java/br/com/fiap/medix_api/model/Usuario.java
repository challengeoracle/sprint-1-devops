package br.com.fiap.medix_api.model;

import br.com.fiap.medix_api.enums.UsuarioRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "TB_MEDI_USUARIO")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @SequenceGenerator(name = "seq_usuario", sequenceName = "SQ_MEDI_USUARIO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @CreationTimestamp
    @Column(name = "dt_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "dt_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @NotNull(message = "O nome não pode ser nulo.")
    @Column(name = "nm_usuario", nullable = false)
    private String nome;

    @NotNull(message = "O e-mail não pode ser nulo.")
    @Column(name = "ds_email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "A senha não pode ser nula.")
    @Column(name = "ds_senha_hash", nullable = false)
    private String senha;

    @NotNull(message = "O CPF não pode ser nulo.")
    @Column(name = "nr_cpf", nullable = false, unique = true)
    private String cpf;

    @NotNull(message = "O tipo de usuário não pode ser nulo.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tp_usuario", nullable = false)
    private UsuarioRole tipoUsuario;

    @PrePersist
    public void prePersist() {
        if (deleted == null) {
            deleted = 0;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(tipoUsuario.getAuthority()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return this.deleted == 0;
    }
}