package com.ecoomerce.autorisation.models;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Authority (role sécurité) utilisé par Spring Security
 */
@NotNull
@Entity
@Table(name = "grdf_authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Authority implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    public Authority(@Size(max = 50) String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
