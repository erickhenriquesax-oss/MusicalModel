package com.fag.musicalmodel.model;


import com.fag.musicalmodel.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_usuario")
@Getter @Setter
public class UsuarioModel implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(length = 70, nullable = false)
    private String nome;

    @Column(length = 100, nullable = false,  unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 100, nullable = false)
    private String senha;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private boolean ativo;

    @Column(length = 11)
    private String cep;

    @Column(length = 100)
    private String endereco;

    @Column(length = 6)
    private Long numero;

    @Column(length = 50)
    private String complemento;

    @Column(length = 13)
    private String telefone;

    @Column()
    private boolean assinatura;

    @Column(length = 5)
    private int horas_estudadas;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_usuario")
    private TipoUsuarioModel tipo_usuario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instrumento")
    private InstrumentoModel instrumentoModel;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "usuarioModel", fetch = FetchType.LAZY)
    private Set<MatriculaModel> matriculaModel = new HashSet<>();

    @OneToOne(mappedBy = "usuarioModel", cascade = CascadeType.ALL)
    private SimuladorModel simuladorModel;

    public UserRole getUserRole() {
        return this.tipo_usuario != null ? this.tipo_usuario.toUserRole() : null;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (getUserRole() != null && getUserRole().getAuthority() != null) {
            return List.of(new SimpleGrantedAuthority(getUserRole().getAuthority()));
        }
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
