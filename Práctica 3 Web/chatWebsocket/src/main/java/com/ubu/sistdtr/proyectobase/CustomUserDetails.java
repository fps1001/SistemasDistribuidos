package com.ubu.sistdtr.proyectobase;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final String id;
    private final UserLevel userLevel;
    private final boolean isInclusive;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String id, UserLevel userLevel, boolean isInclusive) {
        super(username, password, authorities);
        this.id = id;
        this.userLevel = userLevel;
        this.isInclusive = isInclusive;
    }

    public String getId() {
        return id;
    }

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public boolean isInclusive() {
        return isInclusive;
    }
}
