package com.ecoomerce.autorisation.dto;


import com.ecoomerce.autorisation.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * DTO pour l'entité @{@link User}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    @Size(min = 6, max = 8)
    private String gid;
    @Size(max = 50)
    private String firstName;
    @Size(max = 50)
    private String lastName;
    @Email
    @Size(min = 5, max = 255)
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
    @Size(max = 8)
    private String createdBy;
    @Size(max = 8)
    private String modifiedBy;
    private Boolean active;
    private String password;
    private Set<AuthorityDto> authorities;
}
