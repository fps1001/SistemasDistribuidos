package com.ubu.sistdtr.proyectobase;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

/**
 * Esta clase hace falta para extender la clase User del framework Spring Security y añadir la id y la variable isInclusive
 */
public class CustomUserDetails extends User {
    private String id;
    private boolean isInclusive;
    private UserLevel level;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String id, UserLevel level, boolean isInclusive) {
        super(username, password, authorities);
        this.id = id;
        this.isInclusive = isInclusive;
        this.level = level;
    }

    // Getters para los campos adicionales
    public String getId() {
        return id;
    }

    public boolean isInclusive() {
        return isInclusive;
    }
}
