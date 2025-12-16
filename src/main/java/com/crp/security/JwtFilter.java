package com.crp.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crp.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Define the public URLs that this filter should ignore
    private static final List<String> SKIPPED_URLS = Arrays.asList(
        "/api/auth/login",
        "/api/auth/register"
    );

    /**
     * This method is called by Spring to decide if this filter should run for a given request.
     * We return 'true' for our auth endpoints, meaning: "Yes, skip this filter for these URLs."
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Use startsWith for flexibility
        return SKIPPED_URLS.stream().anyMatch(url -> request.getRequestURI().startsWith(url));
    }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException {
        
        // This logic only runs if shouldNotFilter returned 'false' (i.e., for a protected URL)
        String authHeader = req.getHeader("Authorization");
        String token = null, username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Note: user object retrieval is optional if roles are stored in JWT
            // User user = userService.findUserByEmail(username); 
            
            if (jwtUtil.isValid(token, username)) {
                List<String> roles = jwtUtil.getRoles(token);
                List<? extends GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(req, res);
    }
}
