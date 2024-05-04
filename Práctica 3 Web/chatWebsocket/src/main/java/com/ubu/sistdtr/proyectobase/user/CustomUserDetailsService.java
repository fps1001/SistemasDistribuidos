package com.ubu.sistdtr.proyectobase.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación personalizada de UserDetailsService para gestionar la autenticación de usuarios con detalles de usuario personalizados.
 * Utiliza un ConcurrentHashMap para almacenar detalles de usuario y permitir una rápida búsqueda.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private ConcurrentHashMap<String, CustomUserDetails> users = new ConcurrentHashMap<>();

    /**
     * Constructor de CustomUserDetailsService.
     * Inicializa el servicio con una lista de usuarios.
     *
     * @param initialUsers Lista de CustomUserDetails que se añadirán al servicio.
     */
    public CustomUserDetailsService(List<CustomUserDetails> initialUsers) {
        for (CustomUserDetails user : initialUsers) {
            users.put(user.getUsername(), user);
        }
    }

    /**
     * Carga el usuario basado en el nombre de usuario.
     * Este método es utilizado por Spring Security durante el proceso de autenticación.
     *
     * @param username El nombre de usuario del usuario a cargar.
     * @return UserDetails que contiene la información del usuario.
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el nombre de usuario dado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    /**
     * Añade un nuevo usuario al servicio.
     *
     * @param user Los detalles de usuario personalizados del usuario a añadir.
     */
    public void addUser(CustomUserDetails user) {
        users.put(user.getUsername(), user);
    }

    /**
     * Elimina un usuario del servicio basado en el nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a eliminar.
     */
    public void removeUser(String username) {
        users.remove(username);
    }
}
