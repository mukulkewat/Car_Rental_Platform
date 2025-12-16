package com.crp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crp.mapper.UserMapper;
import com.crp.model.Role;
import com.crp.model.User;
import com.crp.repository.RoleRepo;
import com.crp.repository.UserRepo;
import com.crp.requestdto.LoginDTO;
import com.crp.requestdto.UserRegistrationDTO;
import com.crp.responsedto.AuthResponseDTO;
import com.crp.responsedto.UserResponseDTO;
import com.crp.security.JwtUtil;

@Service
public class AuthService {

    @Autowired private UserRepo userRepository;
    @Autowired private RoleRepo roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired(required=true) private UserMapper userMapper;

    public UserResponseDTO register(UserRegistrationDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
        user.setRoles(roles);

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    public AuthResponseDTO login(LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication);

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList();
        System.out.println(roles);
        return new AuthResponseDTO(token, dto.getEmail(), roles);
    }
}
