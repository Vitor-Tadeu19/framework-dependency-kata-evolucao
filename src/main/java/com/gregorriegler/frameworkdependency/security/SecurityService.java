package com.gregorriegler.frameworkdependency.security;

import com.gregorriegler.frameworkdependency.model.User;

public class SecurityService {

    public boolean canViewLibrary(User user) {
        return user != null && (user.hasRole("ROLE_USER") || user.hasRole("ROLE_ADMIN"));
    }

    public boolean canCreateBook(User user) {
        return user != null && user.hasRole("ROLE_ADMIN");
    }

    public boolean canRateBook(User user) {
        return user != null && user.hasRole("ROLE_USER");
    }
}
