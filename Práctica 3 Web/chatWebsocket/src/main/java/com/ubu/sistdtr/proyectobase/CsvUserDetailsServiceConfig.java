package com.ubu.sistdtr.proyectobase;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CsvUserDetailsServiceConfig implements InitializingBean {

    private final List<User.UserBuilder> users = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        // Load user details from a CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(new ClassPathResource("users.csv").getFile()))) {
            String line;
            reader.readLine(); // Skip the header line
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                // Assume the format is Username,Password,Role
                User.UserBuilder userBuilder = User.withUsername(userData[0])
                        .password("{noop}" + userData[1])  // Using {noop} to indicate no encoding is used
                        .roles(userData[2].toUpperCase()); // Convert role to uppercase
                users.add(userBuilder);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read user data from CSV file", e);
        }
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(users.stream().map(User.UserBuilder::build).collect(Collectors.toList()));
    }
}
