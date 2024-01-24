package com.ecoomerce.autorisation.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * L'entité utilisateur
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "grdf_user")
@SuperBuilder
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Matricule GAIA/MAIA
     */
    @Id
    @Column(length = 8, unique = true, nullable = false)
    @NotNull
    @Size(max = 8)
    private String gid;

    /**
     * Prénom
     */
    @Column(name = "first_name", length = 50)
    @Size(max = 50)
    @NotNull
    private String firstName;


    /**
     * password
     */
    @Column(name = "password")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Nom de famille
     */
    @Column(name = "last_name", length = 50)
    @Size(max = 50)
    @NotNull
    private String lastName;
    /**
     * Email de l'utilisateur
     */
    @Column(unique = true)
    @Size(max = 255)
    @Email
    @NotNull
    private String email;

    /**
     * Liste d'authorités
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "grdf_user_authority",
        joinColumns = {@JoinColumn(
            name = "user_gid",
            referencedColumnName = "gid"
        )},
        inverseJoinColumns = {@JoinColumn(
            name = "authority_name",
            referencedColumnName = "name"
        )}
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities;
}
