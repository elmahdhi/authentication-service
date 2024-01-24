package com.ecoomerce.autorisation.mappers;


import com.ecoomerce.autorisation.dto.UserDto;
import com.ecoomerce.autorisation.models.Status;
import com.ecoomerce.autorisation.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper de l'entité {@link ,} et son DTO {@link UserDto}.
 */
@Mapper(componentModel = "spring", imports = {AuthorityMapper.class})
public interface UserMapper extends EntityMapper<UserDto, User>{

    @Named("isActif")
    default boolean isActif(Status status) {
        return status.equals(Status.ACTIF);
    }

    @Mapping(ignore = true, target = "password")
    UserDto toDto(User user);
}
