package com.crp.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.crp.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	
	
    // Custom mapper for roles
    @Named("mapRolesToNames")
    default Set<String> mapRolesToRoleNames(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

}
