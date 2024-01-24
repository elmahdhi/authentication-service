package com.ecoomerce.autorisation.dto;

import com.ecoomerce.autorisation.models.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO pour l'entité @{@link Authority} .
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityDto implements Serializable {

    private String name;
}
