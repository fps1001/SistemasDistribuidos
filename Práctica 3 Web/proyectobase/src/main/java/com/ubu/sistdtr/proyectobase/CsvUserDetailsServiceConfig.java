import org.springframework.beans.factory.InitializingBean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

@Configuration
public class CsvUserDetailsServiceConfig implements InitializingBean {
    private final List<User.UserBuilder> users = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("path/to/users.csv"))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                User.UserBuilder userBuilder = User.withUsername(userData[0])
                        .password("{noop}" + userData[1])  // No password encoding
                        .roles(userData[2].toUpperCase());
                users.add(userBuilder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(users.stream().map(User.UserBuilder::build).collect(Collectors.toList()));
    }
}
