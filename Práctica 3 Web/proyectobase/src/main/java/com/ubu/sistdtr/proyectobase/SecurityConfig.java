package com.ubu.sistdtr.proyectobase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll  // permite a todos acceso al formulario de login
                )
                .httpBasic(httpBasic -> {})  // habilita la autenticación HTTP básica
                .userDetailsService(userDetailsService);  // configura el UserDetailsService personalizado
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Este bean define el encoder de contraseñas. Podría no necesitarlo si las contraseñas están en texto plano
        // Pero lo dejo aquí por completitud y para futura referencia.
        return new BCryptPasswordEncoder();
    }
}
