package com.crp.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crp.exception.BusinessException;
import com.crp.exception.ResourceNotFoundException;
import com.crp.model.Role;
import com.crp.model.User;
import com.crp.repository.UserRepo;

@Service
public class UserService {
	@Autowired
    private final UserRepo userRepo;

    
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // 1️ Find user by ID
    public User findUserById(Long id) {

        return userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id));
    }

    // 2️ Find user by email
    public User findUserByEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("Email must not be empty");
        }

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found with email: " + email);
        }

        return user;
    }

    // 3️ Update user profile
    public User updateUser(User updatedUser) {

        if (updatedUser == null || updatedUser.getId() == null) {
            throw new BusinessException("User ID must be provided for update");
        }

        User existingUser = findUserById(updatedUser.getId());

        // update allowed fields only
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        return userRepo.save(existingUser);
    }
    
    // 4️ Assign roles to user (ADMIN)
    public User assignRoles(Long userId, Set<Role> roles) {

        if (roles == null || roles.isEmpty()) {
            throw new BusinessException("At least one role must be assigned");
        }

        User user = findUserById(userId);
        user.setRoles(roles);

        return userRepo.save(user);
    }
}
