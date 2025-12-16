package com.crp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crp.requestdto.LoginDTO;
import com.crp.requestdto.UserRegistrationDTO;
import com.crp.responsedto.AuthResponseDTO;
import com.crp.responsedto.UserResponseDTO;
import com.crp.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody UserRegistrationDTO dto) {
    	//
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginDTO loginDto) {

        return ResponseEntity.ok(authService.login(loginDto));
    }

    @GetMapping("/greet")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok("Welcome");
    }
}
