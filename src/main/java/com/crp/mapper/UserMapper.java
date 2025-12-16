package com.crp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.crp.model.User;
import com.crp.requestdto.UserRegistrationDTO;
import com.crp.responsedto.UserResponseDTO;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    // DTO → Entity (Registration)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true) // roles assigned separately
    User toEntity(UserRegistrationDTO dto);

    // Entity → Response DTO
    @Mapping(
        source = "roles",
        target = "roles",
        qualifiedByName = "mapRolesToNames"
    )
    UserResponseDTO toResponse(User user);
}
