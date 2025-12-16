package com.crp.restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.crp.model.Role;
import com.crp.service.RoleService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") //  ALL endpoints admin-only
public class AdminController {

    private final RoleService roleService;

    // ✅ Constructor injection (BEST PRACTICE)
    public AdminController(RoleService roleService) {
        this.roleService = roleService;
    }

    // --- Role management endpoints (ADMIN only) ---

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestParam String roleName) {
        return ResponseEntity.ok(roleService.createRole(roleName));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> findAllRoles() {
        return ResponseEntity.ok(roleService.findAllRoles());
    }

    @GetMapping("/roles/{name}")
    public ResponseEntity<Role> findByName(@PathVariable String name) {
        return ResponseEntity.ok(roleService.findByName(name));
    }
}
