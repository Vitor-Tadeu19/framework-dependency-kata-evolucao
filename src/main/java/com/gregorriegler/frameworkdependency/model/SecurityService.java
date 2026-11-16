package com.gregorriegler.frameworkdependency.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

// MELHORIA 1: a classe de dominio nao tem mais @Service.
// A configuracao do Spring ficara em outra camada, fora do modelo.
public class SecurityService {

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
            SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
            SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public boolean isUser() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
            SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
