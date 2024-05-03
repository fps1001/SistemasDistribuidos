package com.ubu.sistdtr.proyectobase.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private final InMemoryUserDetailsManager manager;

    public CustomUserDetailsService(List<UserDetails> users) {
        this.manager = new InMemoryUserDetailsManager(users);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return manager.loadUserByUsername(username);
    }
}
