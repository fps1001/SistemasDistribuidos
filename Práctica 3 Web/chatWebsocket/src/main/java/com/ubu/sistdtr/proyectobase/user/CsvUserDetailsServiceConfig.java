package com.ubu.sistdtr.proyectobase.user;

import com.ubu.sistdtr.proyectobase.model.UserLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Esta clase carga los datos del csv en la variable CustomUserDetails
 */
@Configuration
public class CsvUserDetailsServiceConfig {
    private final List<CustomUserDetails> users = new ArrayList<>();

    @Bean
    public CustomUserDetailsService customUserDetailsService() throws Exception {
        List<CustomUserDetails> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(new ClassPathResource("users.csv").getFile()))) {
            reader.readLine(); // Lee y descarta la primera línea (cabecera)
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(getUserDetails(line));
            }
        }
        return new CustomUserDetailsService(users);
    }

    private static CustomUserDetails getUserDetails(String line) { //Username,Id,Password,Level,Is_inclusive
        String[] userData = line.split(",");
        String username = userData[0].trim();
        String id = userData[1].trim();
        String password = "{noop}" + userData[2].trim();
        UserLevel level = UserLevel.fromString(userData[3].trim());
        boolean isInclusive = Boolean.parseBoolean(userData[4].trim());

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new CustomUserDetails(username, password, authorities, id, level, isInclusive);
    }

}
