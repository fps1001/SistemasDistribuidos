package com.ubu.sistdtr.proyectobase.security;

import com.ubu.sistdtr.proyectobase.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;  // Cambiado para usar CustomUserDetailsService

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll  // permite a todos acceso al formulario de login
                )
                .httpBasic(httpBasic -> {})  // habilita la autenticación HTTP básica
                .userDetailsService(customUserDetailsService);  // configura el UserDetailsService personalizado
        return http.build();
    }
}
