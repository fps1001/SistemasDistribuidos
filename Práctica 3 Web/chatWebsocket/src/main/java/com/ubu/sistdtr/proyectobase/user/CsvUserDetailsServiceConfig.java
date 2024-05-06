package com.ubu.sistdtr.proyectobase.user;

import com.ubu.sistdtr.proyectobase.model.UserLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Configuración para cargar usuarios desde un archivo CSV en objetos CustomUserDetails.
 * Utiliza el archivo 'users.csv' ubicado en los recursos de la clase para obtener los datos de los usuarios.
 *
 * Anotaciones:
 * @Configuration - Indica que esta clase contiene métodos @Bean que deben ser procesados por el contenedor Spring para generar definiciones de bean y solicitudes de servicio.
 */
@Configuration
public class CsvUserDetailsServiceConfig {
    private final List<CustomUserDetails> users = new ArrayList<>();

    /**
     * Crea y retorna una instancia de CustomUserDetailsService.
     * Lee los datos de usuario desde un archivo CSV y los convierte en objetos CustomUserDetails.
     *
     * @return CustomUserDetailsService configurado con usuarios cargados desde un archivo CSV.
     * @throws Exception si ocurre un error al leer el archivo o al procesar los datos de usuario.
     *
     * @Bean - Denota que este método produce un bean manejado por Spring, en este caso, CustomUserDetailsService.
     */
    @Bean
    public CustomUserDetailsService customUserDetailsService() throws Exception {
        List<CustomUserDetails> users = new ArrayList<>();
        //try (BufferedReader reader = new BufferedReader(new FileReader(new ClassPathResource("users.csv").getFile()))) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("users.csv").getInputStream()))) { // cambio filereader por inputstream para dockerizar.
            reader.readLine(); // Lee y descarta la primera línea (cabecera)
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(getUserDetails(line));
            }
        }
        return new CustomUserDetailsService(users);
    }

    /**
     * Convierte una línea del CSV en un objeto CustomUserDetails.
     * Descompone la línea en componentes, crea una lista de autoridades, y construye el objeto CustomUserDetails.
     *
     * @param line Una línea del archivo CSV que contiene los datos del usuario.
     * @return CustomUserDetails creado a partir de los datos en la línea.
     */
    private static CustomUserDetails getUserDetails(String line) { //Username,Id,Password,Level,Is_inclusive
        String[] userData = line.split(",");
        String username = userData[0].trim();
        String id = userData[1].trim();
        String password = "{noop}" + userData[2].trim(); // {noop} indica que la contraseña no está cifrada.
        UserLevel level = UserLevel.fromString(userData[3].trim()); // Convierte el nivel de usuario desde un String.
        boolean isInclusive = Boolean.parseBoolean(userData[4].trim());

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));// Todos los usuarios tienen el rol 'USER'.

        return new CustomUserDetails(username, password, authorities, id, level, isInclusive);
    }

}
