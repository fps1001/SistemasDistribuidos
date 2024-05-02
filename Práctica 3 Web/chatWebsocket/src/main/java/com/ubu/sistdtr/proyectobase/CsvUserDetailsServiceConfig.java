package com.ubu.sistdtr.proyectobase;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Esta clase carga los datos del csv en la variable CustomUserDetails
 */
@Configuration
public class CsvUserDetailsServiceConfig implements InitializingBean {
    private final List<UserDetails> users = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(new ClassPathResource("users.csv").getFile()))) {
            String line = reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                UserDetails user = getUserDetails(line);
                users.add(user);
                System.out.println("Usuarios: " + users);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read user data from CSV file", e);
        }
    }

    private static UserDetails getUserDetails(String line) {
        String[] userData = line.split(",");
        // Example data: Username, Id,Password,Level,Is_inclusive
        String username = userData[0].trim();
        //String id = userData[1].trim();
        String password = "{noop}" + userData[2].trim();
        //UserLevel level = UserLevel.fromString(userData[3].trim());
        //boolean isInclusive = Boolean.parseBoolean(userData[4].trim());

        // Define los permisos que se asignan al usuario.
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));

        return new User(username, password, authorities);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(users);
    }
}
