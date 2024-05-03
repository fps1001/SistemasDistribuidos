package com.ubu.sistdtr.proyectobase.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private ConcurrentHashMap<String, CustomUserDetails> users = new ConcurrentHashMap<>();

    public CustomUserDetailsService(List<CustomUserDetails> initialUsers) {
        for (CustomUserDetails user : initialUsers) {
            users.put(user.getUsername(), user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    public void addUser(CustomUserDetails user) {
        users.put(user.getUsername(), user);
    }

    public void removeUser(String username) {
        users.remove(username);
    }
}
