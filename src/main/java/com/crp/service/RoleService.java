package com.crp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crp.exception.BusinessException;
import com.crp.exception.ResourceNotFoundException;
import com.crp.model.Role;
import com.crp.repository.RoleRepo;

@Service
public class RoleService {
	@Autowired
    private final RoleRepo roleRepository;

    public RoleService(RoleRepo roleRepository) {
        this.roleRepository = roleRepository;
    }

    // 1️⃣ Create new role
    public Role createRole(String roleName) {

        if (roleName == null || roleName.trim().isEmpty()) {
            throw new BusinessException("Role name must not be empty");
        }

        if (roleRepository.findByName(roleName) != null) {
            throw new BusinessException("Role already exists with name: " + roleName);
        }

        Role role = new Role();
        role.setName("ROLE_"+roleName.toUpperCase());

        return roleRepository.save(role);
    }

    // 2️⃣ Get all roles
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    // 3️⃣ Find role by name
    public Role findByName(String roleName) {

        Role role = roleRepository.findByName(roleName);

        if (role == null) {
            throw new ResourceNotFoundException(
                    "Role not found with name: " + roleName);
        }

        return role;
    }
}
