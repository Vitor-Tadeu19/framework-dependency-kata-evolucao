package com.gregorriegler.frameworkdependency.model;

import java.util.Set;

public record User(String name, Set<String> roles) {
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
}
