package fi.haagahelia.quizzer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                        // The REST API endpoints
                        antMatcher("/api/**"),
                        // The error page
                        antMatcher("/error"),
                        // Swagger documentation paths
                        antMatcher("/v3/api-docs/**"),
                        antMatcher("/configuration/ui"),
                        antMatcher("/swagger-resources/**"),
                        antMatcher("/configuration/security"),
                        antMatcher("/swagger-ui/**"))
                // Rest of the permitted paths
                // ...
                .permitAll()
                .anyRequest()
                .authenticated());

        http.formLogin((form) -> form.permitAll());
        http.logout((logout) -> logout.permitAll());
        http.cors(Customizer.withDefaults());
        http.csrf((csrf) -> csrf.ignoringRequestMatchers(antMatcher("/api/**")));

        return http.build();
    }
}