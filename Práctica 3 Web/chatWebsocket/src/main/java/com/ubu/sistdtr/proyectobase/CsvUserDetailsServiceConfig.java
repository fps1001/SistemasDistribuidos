package com.ubu.sistdtr.proyectobase;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CsvUserDetailsServiceConfig implements InitializingBean {

    private final List<UserDetails> users = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(new ClassPathResource("users.csv").getFile()))) {
            String line = reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                // Example data: Username,Id,Password,Level
                boolean isInclusive = Boolean.parseBoolean(userData[3].trim());
                User.UserBuilder userBuilder = User.builder()
                        .username(userData[0])
                        .password("{noop}" + userData[2]); // Contraseña en texto plano
                users.add(userBuilder.build());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read user data from CSV file", e);
        }
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(users);
    }
}
