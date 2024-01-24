package com.ecoomerce.autorisation.mappers;


import com.ecoomerce.autorisation.dto.AuthorityDto;
import com.ecoomerce.autorisation.models.Authority;
import org.mapstruct.Mapper;

/**
 * Mapper de l'entité {@link Authority} et son DTO {@link AuthorityDto}.
 */
@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDto, Authority> {
}
