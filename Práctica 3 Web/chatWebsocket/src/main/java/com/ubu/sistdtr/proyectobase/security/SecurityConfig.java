package com.ubu.sistdtr.proyectobase.security;

import com.ubu.sistdtr.proyectobase.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring Security para la aplicación.
 * Habilita y configura las características de seguridad web, como el control de acceso y la autenticación.
 *
 * Anotaciones:
 * @Configuration - Denota que la clase es una fuente de definiciones de beans para el contexto de la aplicación.
 * @EnableWebSecurity - Habilita la seguridad web de Spring Security en la aplicación, permitiendo la configuración de HTTPSecurity.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired // Inyección de una instancia de CustomUserDetailsService para su uso en la configuración de seguridad.
    private CustomUserDetailsService customUserDetailsService;  // Cambiado para usar CustomUserDetailsService
    /**
     * Define la cadena de filtros de seguridad utilizando HttpSecurity para especificar la configuración.
     *
     * @param http El objeto HttpSecurity que se configura.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception si ocurre un error en la configuración de seguridad.
     */
    @Bean //Indica que el método produce un bean que será gestionado por el contexto de Spring.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Requiere que todas las solicitudes sean autenticadas.
                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll  // permite a todos acceso al formulario de login
                )
                .httpBasic(httpBasic -> {})  // Activa la autenticación HTTP básica con configuración por defecto.
                .userDetailsService(customUserDetailsService);  // configura el UserDetailsService personalizado
        return http.build();
    }
}
