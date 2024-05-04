package com.ubu.sistdtr.proyectobase.user;

import com.ubu.sistdtr.proyectobase.model.UserLevel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * Clase que extiende la funcionalidad de la clase User de Spring Security.
 * Añade detalles específicos del usuario como id, nivel de usuario y estado inclusivo.
 */
public class CustomUserDetails extends User {
    private final String id;
    private final UserLevel userLevel; // Nivel de usuario dentro de la aplicación.
    private final boolean isInclusive; // Indica si el usuario pertenece a un grupo inclusivo.

    /**
     * Constructor para crear una instancia de CustomUserDetails.
     *
     * @param username El nombre de usuario que será usado para autenticar.
     * @param password La contraseña que será usada para autenticar.
     * @param authorities Colección de autoridades concedidas al usuario.
     * @param id Identificador único del usuario.
     * @param userLevel Nivel del usuario definido por la enumeración UserLevel.
     * @param isInclusive Indica si el usuario está marcado como parte de un grupo inclusivo.
     */
    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String id, UserLevel userLevel, boolean isInclusive) {
        super(username, password, authorities);
        this.id = id;
        this.userLevel = userLevel;
        this.isInclusive = isInclusive;
    }

    /**
     * Obtiene el identificador único del usuario.
     * @return el ID del usuario.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nivel del usuario.
     * @return el nivel del usuario según UserLevel.
     */
    public UserLevel getUserLevel() {
        return userLevel;
    }

    /**
     * Determina si el usuario es parte de un grupo inclusivo.
     * @return true si el usuario es inclusivo, false de lo contrario.
     */
    public boolean isInclusive() {
        return isInclusive;
    }
}
